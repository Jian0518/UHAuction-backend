package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.CategoryMapper;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.Category;
import com.utar.uhauction.service.ICategoryService;
import com.utar.uhauction.service.IItemService;
import com.utar.uhauction.service.IItemTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ICategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private IItemTagService IItemTagService;

    @Autowired
    private IItemService IItemService;


    @Override
    public List<Category> insertTags(List<String> tagNames) {
        List<Category> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            Category tag = this.baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, tagName));
            if (tag == null) {
                tag = Category.builder().name(tagName).build();
                this.baseMapper.insert(tag);
            } else {
                tag.setItemCount(tag.getItemCount() + 1);
                this.baseMapper.updateById(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }

    @Override
    public Page<Item> selectItemsByTagId(Page<Item> topicPage, String id) {

        Set<String> ids = IItemTagService.selectItemIdsByTagId(id);
        LambdaQueryWrapper<Item> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Item::getId, ids);

        return IItemService.page(topicPage, wrapper);
    }

}
