package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.BidMapper;
import com.utar.uhauction.model.dto.BidDTO;
import com.utar.uhauction.model.entity.Bid;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.BidVO;
import com.utar.uhauction.service.IBidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class IBidServiceImpl extends ServiceImpl<BidMapper, Bid> implements IBidService {

    @Override
    public List<BidVO> getBidsByItemID(String itemid) {
        List<BidVO> lstBid = new ArrayList<BidVO>();
        try {
            lstBid = this.baseMapper.getBidsByItemID(itemid);
        } catch (Exception e) {
            log.info("lstBid failed");
        }
        return lstBid;
    }

    @Override
    public Bid create(BidDTO dto, User user) {
        Bid bid = Bid.builder()
                .userId(user.getId())
                .amount(dto.getAmount())
                .itemId(dto.getItem_id())
                .bidTime(new Date())
                .build();
        this.baseMapper.insert(bid);
        return bid;
    }
}
