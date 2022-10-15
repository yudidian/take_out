package com.dhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.entity.FavoritesList;
import com.dhy.mapper.FavoritesListMapper;
import com.dhy.service.FavoritesListService;
import org.springframework.stereotype.Service;

@Service
public class FavoritesListImpl extends ServiceImpl<FavoritesListMapper, FavoritesList> implements FavoritesListService {
}
