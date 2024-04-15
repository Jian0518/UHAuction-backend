package com.utar.uhauction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utar.uhauction.model.entity.ItemTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface ItemTagMapper extends BaseMapper<ItemTag> {
    /**
     *
     *
     * @param id
     * @return
     */
    Set<String> getItemIdsByTagId(@Param("id") String id);
}
