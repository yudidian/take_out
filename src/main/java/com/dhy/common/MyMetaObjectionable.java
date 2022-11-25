package com.dhy.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectionable implements MetaObjectHandler {
  @Override
  public void insertFill(MetaObject metaObject) {
    metaObject.setValue("createTime", new Date());
    metaObject.setValue("updateTime", new Date());
    metaObject.setValue("createUser", BaseContext.getCurrentId());
    metaObject.setValue("updateUser", BaseContext.getCurrentId());
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    metaObject.setValue("updateTime", new Date());
    metaObject.setValue("updateUser", BaseContext.getCurrentId());
  }
}
