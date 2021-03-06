package com.dhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dhy.common.CustomException;
import com.dhy.common.R;
import com.dhy.entity.AddressBook;
import com.dhy.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/address")
@ResponseBody
public class AddressBookController {
  @Autowired
  private AddressBookService addressBookService;

  // 添加地址信息
  @PostMapping
  public R<String> save(@RequestBody AddressBook addressBook, HttpSession session) {
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    addressBook.setUserId(userId);
    LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(AddressBook::getUserId,userId);
    int count = addressBookService.count(queryWrapper);
    if (count>3){
      throw new CustomException("每个用户最多设置三个地址");
    }
    boolean save = addressBookService.save(addressBook);
    if (save) {
      return R.success(null,"添加成功");
    }
    return R.error("添加失败");
  }
  // 设置地址为默认地址
  @GetMapping("/set/{id}")
  public R<String> setDefault(@PathVariable Long id , HttpSession session) {
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.eq(AddressBook::getUserId,userId);
    updateWrapper.set(AddressBook::getIsDefault,0);
    addressBookService.update(updateWrapper);
    LambdaUpdateWrapper<AddressBook> updateWrapper2 = new LambdaUpdateWrapper<>();
    updateWrapper2.eq(AddressBook::getId,id);
    updateWrapper2.set(AddressBook::getIsDefault,1);
    addressBookService.update(updateWrapper2);
    return R.success(null, "设置默认地址成功");
  }
  // 获取默认地址
  @GetMapping("/default")
  public R<AddressBook> getDefault(HttpSession session){
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(AddressBook::getUserId,userId);
    queryWrapper.eq(AddressBook::getIsDefault,1);
    AddressBook addressBook = addressBookService.getOne(queryWrapper);
    return R.success(addressBook,"默认地址获取成功");
  }
  // 获取对应用户全部地址列表
  @GetMapping("/list")
  public R<List<AddressBook>> getList(HttpSession session){
    Long userId = Long.valueOf(session.getAttribute("userId").toString());
    LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(AddressBook::getUserId,userId);
    List<AddressBook> list = addressBookService.list(queryWrapper);
    return R.success(list,"用户地址获取成功");
  }

  @GetMapping("/{id}")
  public R<AddressBook> getAddressById(@PathVariable Long id) {
    AddressBook addressBook = addressBookService.getById(id);
    return R.success(addressBook,"对应地址信息获取成功");
  }
}
