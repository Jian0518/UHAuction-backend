package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.ItemTagMapper;
import com.utar.uhauction.model.entity.Category;
import com.utar.uhauction.model.entity.ItemTag;
import com.utar.uhauction.service.IItemTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@Transactional(rollbackFor = Exception.class)
public class IItemTagServiceImpl extends ServiceImpl<ItemTagMapper, ItemTag> implements IItemTagService {

    @Override
    public List<ItemTag> selectByItemId(String itemId) {
        QueryWrapper<ItemTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ItemTag::getItemId, itemId);
        return this.baseMapper.selectList(wrapper);
    }
    @Override
    public void createTopicTag(String id, List<Category> tags) {
        // delete all the records corresponding to item id
        this.baseMapper.delete(new LambdaQueryWrapper<ItemTag>().eq(ItemTag::getItemId, id));

        tags.forEach(tag -> {
            ItemTag itemTag = new ItemTag();
            itemTag.setItemId(id);
            itemTag.setTagId(tag.getId());
            this.baseMapper.insert(itemTag);
        });
    }
    @Override
    public Set<String> selectItemIdsByTagId(String id) {
        return this.baseMapper.getItemIdsByTagId(id);
    }

}
