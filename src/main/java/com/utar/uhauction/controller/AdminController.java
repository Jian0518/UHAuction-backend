package com.utar.uhauction.controller;

import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.model.dto.AdminRequest;
import com.utar.uhauction.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @PostMapping("/login")
    public ApiResult<String> adminLogin(@RequestBody AdminRequest request) {
        if(adminService.verityPasswd(request)){
            return ApiResult.success("Login Success");
        }
        else{
            return ApiResult.failed("Fail to login");
        }
    }
}