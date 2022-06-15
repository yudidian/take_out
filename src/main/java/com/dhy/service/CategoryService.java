package com.dhy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dhy.entity.Category;

public interface CategoryService extends IService<Category> {
  public void myRemove(Long id);
}
