package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.FollowMapper;
import com.utar.uhauction.model.entity.Follow;
import com.utar.uhauction.service.IFollowService;
import org.springframework.stereotype.Service;


@Service
public class IFollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
}
