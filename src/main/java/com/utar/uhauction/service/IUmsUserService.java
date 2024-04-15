package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.dto.LoginDTO;
import com.utar.uhauction.model.dto.RegisterDTO;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.ProfileVO;

import java.util.List;


public interface IUmsUserService extends IService<User> {

    /**
     * register function
     *
     * @param dto
     * @return register object
     */
    User executeRegister(RegisterDTO dto);
    /**
     * get user info
     *
     * @param username
     * @return dbUser
     */
    User getUserByUsername(String username);
    /**
     * user login
     *
     * @param dto
     * @return generated JWT token
     */
    String executeLogin(LoginDTO dto);
    /**
     * get user profile
     *
     * @param id user ID
     * @return
     */
    ProfileVO getUserProfile(String id);

    List<User> allUsers();
}
