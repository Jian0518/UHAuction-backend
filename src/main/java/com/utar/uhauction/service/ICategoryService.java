package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.entity.Item;
import com.utar.uhauction.model.entity.Category;

import java.util.List;


public interface ICategoryService extends IService<Category> {

    List<Category> insertTags(List<String> tags);

    Page<Item> selectItemsByTagId(Page<Item> topicPage, String id);
}
