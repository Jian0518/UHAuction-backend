package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.BillboardMapper;
import com.utar.uhauction.model.entity.Billboard;
import com.utar.uhauction.service.IBillboardService;
import org.springframework.stereotype.Service;

@Service
public class IBillboardServiceImpl extends ServiceImpl<BillboardMapper
        , Billboard> implements IBillboardService {

}
