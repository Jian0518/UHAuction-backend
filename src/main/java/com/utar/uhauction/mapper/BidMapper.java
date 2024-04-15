package com.utar.uhauction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utar.uhauction.model.entity.Bid;
import com.utar.uhauction.model.vo.BidVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidMapper extends BaseMapper<Bid> {
    List<BidVO> getBidsByItemID(@Param("itemid") String itemid);
}
