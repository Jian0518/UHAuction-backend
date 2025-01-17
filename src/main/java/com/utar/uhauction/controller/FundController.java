package com.utar.uhauction.controller;

import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.entity.Fund;
import com.utar.uhauction.service.IFundService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

@RestController
@RequestMapping("/fund")
public class FundController extends BaseController{

    @Resource
    IFundService iFundService;


    @GetMapping("/all")
    public ApiResult<List<Fund>> allFund(){
        return ApiResult.success(iFundService.list());
    }

    @GetMapping("/delete")
    public ApiResult<String> deleteFund(@RequestParam String id){
        iFundService.removeById(id);
        return ApiResult.success("Delete successfully");
    }

    @PostMapping("/update")
    public ApiResult<String > updateFund(@RequestBody Fund fund) {
        iFundService.updateById(fund);
        return ApiResult.success("Update Successfully");
    }

    @PostMapping("/add")
    public ApiResult<String> addFund(@RequestBody Fund fund) {
        iFundService.save(fund);
        return ApiResult.success("Add successfully");
    }
}
