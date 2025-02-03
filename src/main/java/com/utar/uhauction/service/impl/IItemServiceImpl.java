package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.CategoryMapper;
import com.utar.uhauction.mapper.ItemMapper;
import com.utar.uhauction.mapper.ImagesMapper;
import com.utar.uhauction.mapper.UserMapper;
import com.utar.uhauction.model.dto.CreateItemDTO;
import com.utar.uhauction.model.entity.*;
import com.utar.uhauction.model.vo.*;
import com.utar.uhauction.service.*;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class IItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ImagesMapper imagesMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    @Lazy
    private ICategoryService iCategoryService;

    @Autowired
    @Lazy
    private IImagesService iImagesService;

    @Autowired
    private IUmsUserService iUmsUserService;

    @Autowired
    private IItemTagService IItemTagService;

    @Autowired
    private com.utar.uhauction.service.IItemImageService IItemImageService;

    @Override
    public Page<ItemVO> getList(Page<ItemVO> page, String tab) {
        // query item
        Page<ItemVO> iPage = this.baseMapper.selectListAndPage(page, tab);
        // query item's category
        setTopicTags(iPage);
        setFirstItemImage(iPage);
        return iPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Item create(CreateItemDTO dto, User user) {
        Item topic1 = this.baseMapper.selectOne(new LambdaQueryWrapper<Item>().eq(Item::getTitle, dto.getTitle()));
        Assert.isNull(topic1, "Item already exists");

        // encapsulate
        Item item = Item.builder()
                .donorId(user.getId())
                .title(dto.getTitle())
                .content(EmojiParser.parseToAliases(dto.getContent()))
                .createTime(new Date())
                .endTime(dto.getEndTime())
                .cover(dto.getCover())
                .build();
        this.baseMapper.insert(item);

        // user point increment
        int newScore = user.getScore() + 1;
        userMapper.updateById(user.setScore(newScore));


        if (!ObjectUtils.isEmpty(dto.getTags())) {
            //store categories
            List<Category> tags = iCategoryService.insertTags(dto.getTags());
            //process relationship between categories and item
            IItemTagService.createTopicTag(item.getId(), tags);
        }

        if (!ObjectUtils.isEmpty(dto.getImages())) {
            List<Images> images = iImagesService.insertImages(dto.getImages());
            // process relationship between item and image
            IItemImageService.createItemImage(item.getId(), images);
        }

        return item;
    }

    @Override
    public Map<String, Object> viewTopic(String id) {
        Map<String, Object> map = new HashMap<>(16);
        Item item = this.baseMapper.selectById(id);
        // check item detail
        item.setView(item.getView() + 1);
        this.baseMapper.updateById(item);

        item.setContent(EmojiParser.parseToUnicode(item.getContent()));
        map.put("topic", item);

        QueryWrapper<ItemTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ItemTag::getItemId, item.getId());
        Set<String> set = new HashSet<>();
        for (ItemTag articleTag : IItemTagService.list(wrapper)) {
            set.add(articleTag.getTagId());
        }
        List<Category> tags = iCategoryService.listByIds(set);
        map.put("tags", tags);

        List<Images> images = getImagesByItemId(item.getId());
        map.put("images",images);

        // Donor
        ProfileVO user = iUmsUserService.getUserProfile(item.getDonorId());
        map.put("user", user);

        return map;
    }

    @Override
    public BidVO selectHighestBid(String id){
        return this.baseMapper.selectHighestBid(id);
    }

    @Override
    public List<TopContributorVO> selectTopBidder() {
        return this.baseMapper.selectTopBidder();
    }

    @Override
    public List<TopContributorVO> selectTopDonor() {
        return this.baseMapper.selectTopDonor();
    }

    @Override
    public List<FundMonthVO> selectFundByMonth() {
        return this.baseMapper.selectFundByMonth();
    }

    @Override
    public List<TrendCategoryVO> trendCategory() {
        return this.baseMapper.trendCategory();
    }

    @Override
    public List<FundMonthVO> selectItemByMonth() {
        return this.baseMapper.selectItemByMonth();
    }

    @Override
    public List<Item> getRecommend(String id) {
        return this.baseMapper.selectRecommend(id);
    }
    @Override
    public Page<ItemVO> searchByKey(String keyword, Page<ItemVO> page) {
        // query item
        Page<ItemVO> iPage = this.baseMapper.searchByKey(page, keyword);
        // query item's tag
        setTopicTags(iPage);
        return iPage;
    }

    private void setTopicTags(Page<ItemVO> iPage) {
        iPage.getRecords().forEach(topic -> {
            List<ItemTag> itemTags = IItemTagService.selectByItemId(topic.getId());
            if (!itemTags.isEmpty()) {
                List<String> tagIds = itemTags.stream().map(ItemTag::getTagId).collect(Collectors.toList());
                List<Category> tags = categoryMapper.selectBatchIds(tagIds);
                topic.setTags(tags);
            }
        });
    }


    private void setFirstItemImage(Page<ItemVO> iPage) {
        iPage.getRecords().forEach(item -> {
            List<ItemImage> itemImages = IItemImageService.selectByItemId(item.getId());
            if (!itemImages.isEmpty()) {
                ItemImage firstImage = itemImages.get(0); // Selecting the first image
                Images image = imagesMapper.selectById(firstImage.getImgId());
                item.setImage(image);
            }
        });
    }

    @Override
    public List<Images> getImagesByItemId(String id){
        QueryWrapper<ItemImage> wrapperImage = new QueryWrapper<>();
        wrapperImage.lambda().eq(ItemImage::getItemId, id);
        Set<String> imgSet = new HashSet<>();
        for (ItemImage itemImage : IItemImageService.list(wrapperImage)) {
            imgSet.add(itemImage.getImgId());
        }
        List<Images> images = iImagesService.listByIds(imgSet);
        return images;
    }

    @Override
    public void removeImageById(String id){
        iImagesService.removeById(id);
    }

}
