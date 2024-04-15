package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.dto.BidDTO;
import com.utar.uhauction.model.entity.Bid;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.BidVO;

import java.util.List;

public interface IBidService extends IService<Bid> {
    /**
     *
     *
     * @param itemid
     * @return {@link Bid}
     */
    List<BidVO> getBidsByItemID(String itemid);

    Bid create(BidDTO dto, User principal);
}