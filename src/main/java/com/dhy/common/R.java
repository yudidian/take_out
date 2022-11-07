package com.dhy.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class R<T> implements Serializable {

  private Integer code; //编码：1成功，0和其它数字为失败

  private String msg; //错误信息

  private T info; //数据

  private Map<String,Object> data = new HashMap<>(); //动态数据

  public static <T> R<T> success(T object,String msg) {
    R<T> r = new R<>();
    r.info = object;
    r.code = 1;
    r.msg = msg;
    return r;
  }
  public static <T> R<T> SuccessPlus(Map<String,Object> map,String msg) {
    R<T> r = new R<>();
    r.code = 1;
    r.msg = msg;
    r.data.putAll(map);
    return r;
  }

  public static <T> R<T> error(String msg) {
    R<T> r = new R<>();
    r.msg = msg;
    r.code = 0;
    return r;
  }

  public R<T> add(String key, Object value) {
    this.data.put(key, value);
    return this;
  }

}
