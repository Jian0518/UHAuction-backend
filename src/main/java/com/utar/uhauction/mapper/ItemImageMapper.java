package com.utar.uhauction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utar.uhauction.model.entity.ItemImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ItemImageMapper extends BaseMapper<ItemImage> {

    /**
     *
     *
     * @param id
     * @return
     */
    Set<String> getItemIdsByImageId(@Param("id") String id);

}
