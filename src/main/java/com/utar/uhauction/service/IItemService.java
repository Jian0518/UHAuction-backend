package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.dto.CreateItemDTO;
import com.utar.uhauction.model.entity.Images;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.User;
import com.utar.uhauction.model.vo.*;

import java.util.List;
import java.util.Map;


public interface IItemService extends IService<Item> {

    /**
     * get item for main page
     *
     * @param page
     * @param tab
     * @return
     */
    Page<ItemVO> getList(Page<ItemVO> page, String tab);

    Item create(CreateItemDTO dto, User principal);

    /**
     * view item detail
     *
     * @param id
     * @return
     */
    Map<String, Object> viewTopic(String id);
    /**
     * randomly recommend 10 items
     *
     * @param id
     * @return
     */
    List<Item> getRecommend(String id);
    /**
     * query by keyword
     *
     * @param keyword
     * @param page
     * @return
     */
    Page<ItemVO> searchByKey(String keyword, Page<ItemVO> page);

    List<Images> getImagesByItemId(String id);

    void removeImageById(String id);

    BidVO selectHighestBid(String id);

    List<TopContributorVO> selectTopBidder();
    List<TopContributorVO> selectTopDonor();

    List<FundMonthVO> selectFundByMonth();
    List<TrendCategoryVO> trendCategory();
    List<FundMonthVO> selectItemByMonth();
}
