package com.utar.uhauction.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.dto.LoginDTO;
import com.utar.uhauction.model.dto.RegisterDTO;
import com.utar.uhauction.model.entity.*;
import com.utar.uhauction.service.IItemService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/ums/user")
public class UmsUserController extends BaseController {

    @Resource
    private IUmsUserService iUmsUserService;
    @Resource
    private IItemService iItemService;

    @Autowired
    private com.utar.uhauction.service.IBidService IBidService;


    @GetMapping("/username")
    public ApiResult<String> getusernameByID(@RequestParam(value = "userid", defaultValue = "") String userid) {
        User user = iUmsUserService.getById(userid);
        return ApiResult.success(user.getAlias());
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResult<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        User user = iUmsUserService.executeRegister(dto);
        if (ObjectUtils.isEmpty(user)) {
            return ApiResult.failed("Register Failed");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        return ApiResult.success(map);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> login(@Valid @RequestBody LoginDTO dto) {
        String token = iUmsUserService.executeLogin(dto);
        if (ObjectUtils.isEmpty(token)) {
            return ApiResult.failed("Wrong password");
        }
        Map<String, String> map = new HashMap<>(16);
        map.put("token", token);
        return ApiResult.success(map, "Login successfully");
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ApiResult<User> getUser(@RequestHeader(value = USER_NAME) String userName) {
        User user = iUmsUserService.getUserByUsername(userName);
        return ApiResult.success(user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ApiResult<Object> logOut() {
        return ApiResult.success(null, "Log out success");
    }


    @GetMapping()
    public ApiResult<List<User>> allUser(){
        //List<User> users = iUmsUserService.allUsers();
        //Map<String, Object> map = new HashMap<>(16);
        //map.put("users",users);
        return ApiResult.success(iUmsUserService.allUsers());
    }


    @GetMapping("/{username}")
    public ApiResult<Map<String, Object>> getUserByName(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        User user = iUmsUserService.getUserByUsername(username);
        Assert.notNull(user, "User does not exist");

        // Handle donated items
        Page<Item> donatedPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>().eq(Item::getDonorId, user.getId()));

        // Handle bids
        QueryWrapper<Bid> wrapperBid = new QueryWrapper<>();
        wrapperBid.lambda().eq(Bid::getUserId, user.getId());
        List<Bid> bids = IBidService.list(wrapperBid);

        Page<Item> bidPage;

        if (!bids.isEmpty()) {
            Set<String> itemSet = bids.stream().map(Bid::getItemId).collect(Collectors.toSet());
            List<Item> allBidItems = iItemService.listByIds(itemSet);
            List<Item> pageItems = allBidItems.stream()
                    .skip((pageNo - 1) * size)
                    .limit(size)
                    .collect(Collectors.toList());

            bidPage = new Page<>(pageNo, size);
            bidPage.setRecords(pageItems);
            bidPage.setTotal(allBidItems.size()); // set total to the total number of items that match the criteria
        } else {
            // Return an empty page
            bidPage = new Page<>(pageNo, size);
            bidPage.setRecords(Collections.emptyList());
            bidPage.setTotal(0); // Set total to 0 since there are no items
        }


        // Handle won items
        LocalDate today = LocalDate.now();
        Page<Item> wonPage = iItemService.page(new Page<>(pageNo, size),
                new LambdaQueryWrapper<Item>()
                        .eq(Item::getWinnerId, user.getId())
                        .le(Item::getEndTime, today));

        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        map.put("items", donatedPage);
        map.put("bidPage", bidPage);
        map.put("wonPage", wonPage);
        return ApiResult.success(map);
    }

    @PostMapping("/update")
    public ApiResult<User> updateUser(@RequestBody User user) {
        iUmsUserService.updateById(user);
        return ApiResult.success(user);
    }

    @GetMapping("/delete")
    public ApiResult<String> deleteUser(@RequestParam String id){
        iUmsUserService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

}
