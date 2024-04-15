package com.utar.uhauction.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.vo.ItemVO;
import com.utar.uhauction.service.IItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/search")
public class SearchController extends BaseController {

    @Resource
    private IItemService itemService;

    @GetMapping
    public ApiResult<Page<ItemVO>> searchList(@RequestParam("keyword") String keyword,
                                              @RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize) {
        Page<ItemVO> results = itemService.searchByKey(keyword, new Page<>(pageNum, pageSize));
        return ApiResult.success(results);
    }

}
