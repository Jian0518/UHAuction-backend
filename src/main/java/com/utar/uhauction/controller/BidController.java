package com.utar.uhauction.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.mapper.ItemMapper;
import com.utar.uhauction.model.dto.BidDTO;
import com.utar.uhauction.model.entity.Bid;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.BidVO;
import com.utar.uhauction.service.IBidService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;

@RestController
@RequestMapping("/bid")
public class BidController extends BaseController {
    @Resource
    private IBidService bidService;
    @Resource
    private IUmsUserService umsUserService;
    @Resource
    private ItemMapper itemMapper;
    @PostMapping("/add_bid")
    public ApiResult<Bid> add_bid(@RequestHeader(value = USER_NAME) String userName,
                                  @RequestBody BidDTO dto) {
        User user = umsUserService.getUserByUsername(userName);

        Item item = itemMapper.selectById(dto.getItem_id());
        item.setBidCount(item.getBidCount()+1);

        //if the amount is higher than the current highest bid,
        //update the winner and highest bid
        if(dto.getAmount()>item.getHighestBid()){
            item.setWinnerId(user.getId());
            item.setHighestBid(dto.getAmount());
        }

        itemMapper.updateById(item);

        Bid bid = bidService.create(dto, user);
        return ApiResult.success(bid);
    }

    @GetMapping("/avgBid")
    public ApiResult<Integer> getAvgBid(){
        return ApiResult.success(bidService.getAvgBid());
    }

    @GetMapping("/get_bids")
    public ApiResult<List<BidVO>> getBidsByItemID(@RequestParam(value = "itemid", defaultValue = "1") String itemid) {
        List<BidVO> lstBmsComment = bidService.getBidsByItemID(itemid);
        return ApiResult.success(lstBmsComment);
    }

    @GetMapping("/all")
    public ApiResult<List<Bid>> allBid(){
        return ApiResult.success(bidService.list());
    }


    @PostMapping("/update_bid")
    public ApiResult<Bid> update_bid(@RequestHeader(value = USER_NAME) String userName, @RequestBody Bid bid) {
        User user = umsUserService.getUserByUsername(userName);
        Assert.isTrue(user.getId().equals(bid.getUserId()), "Only author can edit");
        bidService.updateById(bid);

        Item item = itemMapper.selectById(bid.getItemId());

        if(bid.getAmount()>item.getHighestBid()){
            item.setWinnerId(user.getId());
            item.setHighestBid(bid.getAmount());
        }

        itemMapper.updateById(item);

        return ApiResult.success(bid);
    }

    @GetMapping("/bidders")
    public ApiResult<List<BidVO>> getBidder(@RequestParam(value = "itemid", defaultValue = "1") String itemid){
        List<BidVO> bidders = bidService.getBidsByItemID(itemid);
        return ApiResult.success(bidders);
    }
}
