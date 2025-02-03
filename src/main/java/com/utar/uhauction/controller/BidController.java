package com.utar.uhauction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utar.uhauction.common.api.ApiResult;
import com.utar.uhauction.config.BidWebSocketHandler;
import com.utar.uhauction.mapper.ItemMapper;
import com.utar.uhauction.model.dto.BidDTO;
import com.utar.uhauction.model.entity.Bid;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.BidVO;
import com.utar.uhauction.service.IBidService;
import com.utar.uhauction.service.IUmsUserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.utar.uhauction.jwt.JwtUtil.USER_NAME;

@RestController
@RequestMapping("/bid")
public class BidController extends BaseController {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Object BID_LOCK = new Object();

    @Resource
    private IBidService bidService;
    @Resource
    private IUmsUserService umsUserService;
    @Resource
    private ItemMapper itemMapper;
    @PostMapping("/add_bid")
    @CacheEvict(value = "itemDetails", key = "#dto.item_id")
    public ApiResult<Bid> add_bid(@RequestHeader(value = USER_NAME) String userName,
                                  @RequestBody BidDTO dto) {
        User user = umsUserService.getUserByUsername(userName);
        
        synchronized (BID_LOCK) {
            Item item = itemMapper.selectById(dto.getItem_id());
            if(dto.getAmount() > item.getHighestBid()){
                item.setWinnerId(user.getId());
                item.setHighestBid(dto.getAmount());
                item.setBidCount(item.getBidCount()+1);
                itemMapper.updateById(item);
                
                Bid bid = bidService.create(dto, user);

                // Broadcast new bid to all connected clients
                try {
                    String bidJson = objectMapper.writeValueAsString(bid);
                    BidWebSocketHandler.broadcast(bidJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return ApiResult.success(bid);
            } else {
                return ApiResult.failed("Bid amount is lower than the current highest bid");
            }
        }
    }
    @PostMapping("/update_bid")
    @CacheEvict(value = "itemDetails", key = "#bid.itemId")
    public ApiResult<Bid> update_bid(@RequestHeader(value = USER_NAME) String userName, @RequestBody Bid bid) {
        User user = umsUserService.getUserByUsername(userName);
        Assert.isTrue(user.getId().equals(bid.getUserId()), "Only author can edit");

        synchronized (BID_LOCK) {
            Item item = itemMapper.selectById(bid.getItemId());
            if(bid.getAmount() > item.getHighestBid()){
                item.setWinnerId(user.getId());
                item.setHighestBid(bid.getAmount());
                itemMapper.updateById(item);
                bidService.updateById(bid);
                // Broadcast updated bid to all clients
                try {
                    String bidJson = objectMapper.writeValueAsString(bid);
                    System.out.println(bidJson);
                    BidWebSocketHandler.broadcast(bidJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ApiResult.success(bid);
            } else {
                return ApiResult.failed("Bid amount is lower than the current highest bid");
            }
        }
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

    @GetMapping("/bidders")
    public ApiResult<List<BidVO>> getBidder(@RequestParam(value = "itemid", defaultValue = "1") String itemid){
        List<BidVO> bidders = bidService.getBidsByItemID(itemid);
        return ApiResult.success(bidders);
    }
}
