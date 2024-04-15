package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.entity.Images;
import com.utar.uhauction.model.entity.ItemImage;

import java.util.List;
import java.util.Set;

public interface IItemImageService extends IService<ItemImage> {


    /**
     * get Topic Tag records
     *
     * @param itemId ItemId
     * @return
     */
    List<ItemImage> selectByItemId(String itemId);
    /**
     * create relationship
     *
     * @param id
     * @param images
     * @return
     */
    void createItemImage(String id, List<Images> images);
    /**
     *
     *
     * @param id
     * @return
     */
    Set<String> selectItemIdsByImageId(String id);

}

