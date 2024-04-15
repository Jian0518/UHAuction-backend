package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.entity.Category;
import com.utar.uhauction.model.entity.ItemTag;

import java.util.List;
import java.util.Set;

public interface IItemTagService extends IService<ItemTag> {


    List<ItemTag> selectByItemId(String itemId);

    void createTopicTag(String id, List<Category> tags);

    Set<String> selectItemIdsByTagId(String id);

}
