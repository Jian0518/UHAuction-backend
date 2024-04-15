package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.entity.Images;

import java.util.List;

public interface IImagesService extends IService<Images> {
    List<Images> insertImages(List<String> images);
}
