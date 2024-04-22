package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.PaymentMapper;
import com.utar.uhauction.model.entity.Payment;
import com.utar.uhauction.service.IPaymentService;
import org.springframework.stereotype.Service;

@Service
public class IPaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {
}
