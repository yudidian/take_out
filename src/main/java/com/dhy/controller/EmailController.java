package com.dhy.controller;

import com.dhy.common.R;
import com.dhy.service.EmailService;
import com.dhy.utils.RedisUtil;
import com.dhy.utils.ValidateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/email")
@ResponseBody
public class EmailController {
  @Autowired
  private EmailService emailService;

  @Resource
  private RedisUtil redisUtil;

  @GetMapping
  public R<String> sendEmailCode(String email){
    ValidateCodeUtil codeUtil = new ValidateCodeUtil(redisUtil);
    emailService.send(email,"kola 烘焙坊：验证码",codeUtil.getCode(email));
    return R.success(null,"发送验证码成功");
  }
}
