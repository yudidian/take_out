package com.dhy.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局处理异常
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
    log.info("错误信息 {}",e.getMessage());
    if (e.getMessage().contains("Duplicate entry")){
      String[] split = e.getMessage().split(" ");
      String msg = split[2] + "已存在";
      return R.error(msg);
    }
    return R.error("未知服务器添加错误");
  }
  @ExceptionHandler(CustomException.class)
  public R<String> exceptionHandler(CustomException e){
    return R.error(e.getMessage());
  }
}
