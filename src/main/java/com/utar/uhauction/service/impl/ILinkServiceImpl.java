package com.utar.uhauction.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.LinkMapper;
import com.utar.uhauction.model.entity.Link;
import com.utar.uhauction.service.ILinkService;
import org.springframework.stereotype.Service;

@Service
public class ILinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

}
