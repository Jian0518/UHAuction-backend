package com.utar.uhauction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utar.uhauction.mapper.FavoriteMapper;
import com.utar.uhauction.model.entity.Favorite;
import com.utar.uhauction.service.IFavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IFavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {
    @Override
    public void create(Favorite favorite) {

        this.baseMapper.insert(favorite);
    }
}
