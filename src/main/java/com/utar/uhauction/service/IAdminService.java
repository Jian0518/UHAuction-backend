package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.dto.AdminRequest;
import com.utar.uhauction.model.entity.Admin;

public interface IAdminService extends IService<Admin> {
    boolean verityPasswd(AdminRequest adminRequest);
}
