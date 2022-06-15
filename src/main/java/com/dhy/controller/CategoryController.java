package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhy.common.R;
import com.dhy.entity.Category;
import com.dhy.entity.Dish;
import com.dhy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@ResponseBody
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  /**
   * 增添菜品分类
   */
  @PostMapping
  public R<String> save(@RequestBody Category category) {
    categoryService.save(category);
    return R.success(null, "添加成功");
  }

  /**
   * 菜品分页
   */
  @GetMapping
  public R<Page> getCategoryPage(int page, int pageSize) {
    Page pageInfo = new Page(page, pageSize);
    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Category::getSort);
    categoryService.page(pageInfo, queryWrapper);
    return R.success(pageInfo, "菜品页面数据获取成功");
  }
  /**
   * 删除商品
   */
  @DeleteMapping("{id}")
  public R<String> deleteOne(@PathVariable Long id){
    categoryService.myRemove(id);
    return R.success(null,"删除成功");
  }

  // 修改商品分类
  @PutMapping
  public R<String> update(@RequestBody Category category){
    categoryService.updateById(category);
    return R.success(null,"修改成功");
  }

  // 获取商品分类列表
  @GetMapping("/all")
  public R<List> getAllCategory(){
    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Category::getType);
    return getListR(queryWrapper);
  }

  private R<List> getListR(LambdaQueryWrapper<Category> queryWrapper) {
    List<Category> categories = categoryService.list(queryWrapper);
    List<Map<String, Object>> collect = categories.stream().map(item -> {
      Map<String, Object> map = new HashMap<>();
      map.put("id", item.getId());
      map.put("type", item.getType());
      map.put("name", item.getName());
      return map;
    }).collect(Collectors.toList());
    return R.success(collect,"获取成功");
  }

  // 获取商品分类列表
  @GetMapping("/list/{type}")
  public R<List> list(@PathVariable int type){
    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Category::getType,type);
    return getListR(queryWrapper);
  }
}
