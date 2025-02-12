package com.utar.uhauction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.utar.uhauction.model.entity.Category;
import com.utar.uhauction.model.vo.CategoryVO;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryMapper extends BaseMapper<Category> {
    List<CategoryVO> getCategoryStatistic();
}
