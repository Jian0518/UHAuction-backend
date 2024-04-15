package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.FundMapper;
import com.utar.uhauction.model.entity.Fund;
import com.utar.uhauction.service.IFundService;
import org.springframework.stereotype.Service;

@Service
public class IFundServiceImpl extends ServiceImpl<FundMapper, Fund> implements IFundService {

}
