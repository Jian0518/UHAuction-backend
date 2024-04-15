package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.ImagesMapper;
import com.utar.uhauction.model.entity.Images;
import com.utar.uhauction.service.IImagesService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class IImageServiceImpl extends ServiceImpl<ImagesMapper, Images> implements IImagesService {
    @Override
    public List<Images> insertImages(List<String> imageNames) {
        List<Images> imageList = new ArrayList<>();
        for (String imageName : imageNames) {
            Images tag = this.baseMapper.selectOne(new LambdaQueryWrapper<Images>().eq(Images::getName, imageName));
            if (tag == null) {
                tag = Images.builder().name(imageName).build();
                this.baseMapper.insert(tag);
            }
            imageList.add(tag);
        }
        return imageList;
    }
}
