package com.utar.uhauction.controller;

import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.entity.Link;
import com.utar.uhauction.service.ILinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Resource
    private ILinkService linkService;

    @GetMapping("/all")
    public ApiResult<List<Link>> getRandomTip() {
        List<Link> links = linkService.list();

        return ApiResult.success(links);
    }

}
