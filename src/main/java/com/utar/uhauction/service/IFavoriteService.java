package com.utar.uhauction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.utar.uhauction.model.entity.Favorite;

public interface IFavoriteService extends IService<Favorite> {
    public void create(Favorite favorite);
}
