package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.ItemImageMapper;
import com.utar.uhauction.model.entity.Images;
import com.utar.uhauction.model.entity.ItemImage;
import com.utar.uhauction.service.IItemImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
@Service
@Transactional(rollbackFor = Exception.class)
public class IItemImageServiceImpl extends ServiceImpl<ItemImageMapper, ItemImage> implements IItemImageService {
    @Override
    public List<ItemImage> selectByItemId(String itemId) {
        QueryWrapper<ItemImage> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ItemImage::getItemId, itemId);
        return this.baseMapper.selectList(wrapper);
    }
    @Override
    public void createItemImage(String id, List<Images> images) {
        // first, delete all the records according to item id
        this.baseMapper.delete(new LambdaQueryWrapper<ItemImage>().eq(ItemImage::getItemId, id));


        images.forEach(image -> {
            ItemImage itemImage = new ItemImage();
            itemImage.setItemId(id);
            itemImage.setImgId(image.getId());
            this.baseMapper.insert(itemImage);
        });
    }
    @Override
    public Set<String> selectItemIdsByImageId(String id) {
        return this.baseMapper.getItemIdsByImageId(id);
    }
}
