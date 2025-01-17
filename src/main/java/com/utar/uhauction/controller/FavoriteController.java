package com.utar.uhauction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.entity.Favorite;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.ItemVO;
import com.utar.uhauction.service.IFavoriteService;
import com.utar.uhauction.service.IItemService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    private IItemService iItemService;
    @Resource
    private IUmsUserService iUmsUserService;
    @Resource
    private IFavoriteService favoriteService;
    @PostMapping("/add/{userId}")
    public ApiResult<Favorite> add_favorite(@PathVariable("userId") String userId,
                                            @RequestBody ItemVO dto) {
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setName(dto.getTitle());
        favorite.setItemId(dto.getId());

        favoriteService.create(favorite);

        return ApiResult.success(null,"Added to favorite list");
    }

    @GetMapping("/list/{username}")
    public ApiResult<Map<String, Object>> getUserByName(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        User user = iUmsUserService.getUserByUsername(username);
        Assert.notNull(user, "User does not exist");

        // get the list of favorite items
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.lambda().eq(Favorite::getUserId, user.getId());
        List<Favorite> favorites = favoriteService.list(favoriteQueryWrapper);
        Page<Item> favoritePage;

        if (!favorites.isEmpty()) {
            Set<String> itemSet = favorites.stream().map(Favorite::getItemId).collect(Collectors.toSet());
            List<Item> allFavoriteItem = iItemService.listByIds(itemSet);
            List<Item> pageItems = allFavoriteItem.stream()
                    .skip((pageNo - 1) * size)
                    .limit(size)
                    .collect(Collectors.toList());

            favoritePage = new Page<>(pageNo, size);
            favoritePage.setRecords(pageItems);
            favoritePage.setTotal(allFavoriteItem.size()); // set total to the total number of items that match the criteria
        } else {
            // Return an empty page
            favoritePage = new Page<>(pageNo, size);
            favoritePage.setRecords(Collections.emptyList());
            favoritePage.setTotal(0); // Set total to 0 since there are no items
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("user", user);
        map.put("items", favoritePage);

        return ApiResult.success(map);
    }

}
