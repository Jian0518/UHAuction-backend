package com.utar.uhauction.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.Category;
import com.utar.uhauction.service.ICategoryService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @Resource
    private ICategoryService bmsTagService;

    @GetMapping("/list")
    public ApiResult<List<Category>> getCategoryList() {
        return ApiResult.success(bmsTagService.list());
    }




    @GetMapping("/{name}")
    public ApiResult<Map<String, Object>> getItemsByTag(
            @PathVariable("name") String tagName,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Map<String, Object> map = new HashMap<>(16);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, tagName);
        Category one = bmsTagService.getOne(wrapper);
        Assert.notNull(one, "The item does not exist or deleted by admin");
        Page<Item> topics = bmsTagService.selectItemsByTagId(new Page<>(page, size), one.getId());
        // other hot categories
        Page<Category> hotTags = bmsTagService.page(new Page<>(1, 10),
                new LambdaQueryWrapper<Category>()
                        .notIn(Category::getName, tagName)
                        .orderByDesc(Category::getItemCount));

        map.put("topics", topics);
        map.put("hotTags", hotTags);

        return ApiResult.success(map);
    }

}
