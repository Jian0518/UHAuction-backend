package com.utar.uhauction.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.dto.CreateItemDTO;
import com.utar.uhauction.model.entity.*;
import com.utar.uhauction.model.vo.ItemVO;
import com.utar.uhauction.service.IFundService;
import com.utar.uhauction.service.IItemService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;


@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Resource
    IFundService iFundService;

    @Resource
    private IItemService iItemService;
    @Resource
    private IUmsUserService umsUserService;

    @GetMapping("/list")
    public ApiResult<Page<ItemVO>> list(@RequestParam(value = "tab", defaultValue = "ongoing") String tab,
                                        @RequestParam(value = "pageNo", defaultValue = "1")  Integer pageNo,
                                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
        Page<ItemVO> list = iItemService.getList(new Page<>(pageNo, pageSize), tab);
        return ApiResult.success(list);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ApiResult<Item> create(@RequestHeader(value = USER_NAME) String userName
            , @RequestBody CreateItemDTO dto) {
        User user = umsUserService.getUserByUsername(userName);
        Item item = iItemService.create(dto, user);
        return ApiResult.success(item);
    }
    @GetMapping()
    public ApiResult<Map<String, Object>> view(@RequestParam("id") String id) {
        Map<String, Object> map = iItemService.viewTopic(id);
        return ApiResult.success(map);
    }

    @GetMapping("/recommend")
    public ApiResult<List<Item>> getRecommend(@RequestParam("itemId") String id) {
        List<Item> items = iItemService.getRecommend(id);
        return ApiResult.success(items);
    }

    @PostMapping("/update")
    public ApiResult<Item> update(@RequestHeader(value = USER_NAME) String userName, @Valid @RequestBody Item item) {
        User user = umsUserService.getUserByUsername(userName);
        Assert.isTrue(user.getId().equals(item.getDonorId()), "Only author can edit");
        item.setModifyTime(new Date());
        iItemService.updateById(item);
        return ApiResult.success(item);
    }

    @PostMapping("/admin/update")
    public ApiResult<Item> adminUpdate( @RequestBody Item item) {
        item.setModifyTime(new Date());
        iItemService.updateById(item);
        return ApiResult.success(item);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResult<String> delete(@RequestHeader(value = USER_NAME) String userName, @PathVariable("id") String id) {
        User user = umsUserService.getUserByUsername(userName);
        Item byId = iItemService.getById(id);
        Assert.notNull(byId, "The item does not exist");
        Assert.isTrue(byId.getDonorId().equals(user.getId()), "Why you can delete other people's item");
        iItemService.removeById(id);
        return ApiResult.success(null,"Delete success");
    }

    @GetMapping("/all")
    public ApiResult<List<Item>> allItem(){
        return ApiResult.success(iItemService.list());
    }

    @GetMapping("/admin/delete")
    public ApiResult<String> deleteUser(@RequestParam String id){
        iItemService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

    @GetMapping("/images")
    public ApiResult<List<Images>> getImagesByItemId(@RequestParam String id){
        List<Images> imagesByItemId = iItemService.getImagesByItemId(id);
        return ApiResult.success(imagesByItemId);
    }

    @GetMapping("/image/delete")
    public ApiResult<String> deleteComment(@RequestParam String id){
        iItemService.removeImageById(id);
        return ApiResult.success("Delete successfully");
    }


    @GetMapping("/fund/all")
    public ApiResult<List<Fund>> allFund(){
        return ApiResult.success(iFundService.list());
    }

    @GetMapping("/fund/delete")
    public ApiResult<String> deleteFund(@RequestParam String id){
        iFundService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

    @PostMapping("/fund/update")
    public ApiResult<String > updateFund(@RequestBody Fund fund) {
        iFundService.updateById(fund);
        return ApiResult.success("Update Successfully");
    }

    @PostMapping("/fund/add")
    public ApiResult<String> addFund(@RequestBody Fund fund) {
        System.out.println("Add fund");
        iFundService.save(fund);
        return ApiResult.success("Add successfully");
    }
}
