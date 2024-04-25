package com.utar.uhauction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.vo.BidVO;
import com.utar.uhauction.model.vo.ItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMapper extends BaseMapper<Item> {
    /**
     * Query the home page topic list by pagination
     * <p>
     *
     * @param page
     * @param tab
     * @return
     */
    Page<ItemVO> selectListAndPage(@Param("page") Page<ItemVO> page, @Param("tab") String tab);

    /**
     * Get details page recommendations
     *
     * @param id
     * @return
     */
    List<Item> selectRecommend(@Param("id") String id);
    /**
     * Full Text Search
     *
     * @param page
     * @param keyword
     * @return
     */
    Page<ItemVO> searchByKey(@Param("page") Page<ItemVO> page, @Param("keyword") String keyword);

    BidVO selectHighestBid(String id);
}
