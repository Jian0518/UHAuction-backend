package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.common.exception.ApiAsserts;
import com.utar.uhauction.jwt.JwtUtil;
import com.utar.uhauction.mapper.FollowMapper;
import com.utar.uhauction.mapper.ItemMapper;
import com.utar.uhauction.mapper.UserMapper;
import com.utar.uhauction.model.dto.LoginDTO;
import com.utar.uhauction.model.dto.RegisterDTO;
import com.utar.uhauction.model.entity.Follow;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.ProfileVO;
import com.utar.uhauction.service.IUmsUserService;
import com.utar.uhauction.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IUmsUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUmsUserService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private FollowMapper followMapper;

    @Override
    public User executeRegister(RegisterDTO dto) {
        //check if there is duplicated user name
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getName()).or().eq(User::getEmail, dto.getEmail());
        User user = baseMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(user)) {
            ApiAsserts.fail("id or email already existed！");
        }
        User addUser = User.builder()
                .username(dto.getName())
                .alias(dto.getName())
                .password(MD5Utils.getPwd(dto.getPass()))
                .email(dto.getEmail())
                .createTime(new Date())
                .status(true)
                .build();
        baseMapper.insert(addUser);

        return addUser;
    }
    @Override
    public User getUserByUsername(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }
    @Override
    public String executeLogin(LoginDTO dto) {
        String token = null;
        try {
            User user = getUserByUsername(dto.getUsername());
            String encodePwd = MD5Utils.getPwd(dto.getPassword());
            if(!encodePwd.equals(user.getPassword()))
            {
                throw new Exception("Incorrect Password");
            }
            token = JwtUtil.generateToken(String.valueOf(user.getUsername()));
        } catch (Exception e) {
            log.warn("User not existed or wrong password=======>{}", dto.getUsername());
        }
        return token;
    }
    @Override
    public ProfileVO getUserProfile(String id) {
        ProfileVO profile = new ProfileVO();
        User user = baseMapper.selectById(id);
        BeanUtils.copyProperties(user, profile);
        // user's donated item
        int count = itemMapper.selectCount(new LambdaQueryWrapper<Item>().eq(Item::getDonorId, id));
        profile.setItemCount(count);

        // amount of follower
        int followers = followMapper.selectCount((new LambdaQueryWrapper<Follow>().eq(Follow::getParentId, id)));
        profile.setFollowerCount(followers);

        return profile;
    }

    @Override
    public List<User> allUsers() {
        return baseMapper.selectList(null);
    }
}
