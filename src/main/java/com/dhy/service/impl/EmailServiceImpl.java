package com.dhy.service.impl;


import com.dhy.common.CustomException;
import com.dhy.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSenderImpl mailSender;
  @Autowired
  private TemplateEngine templateEngine;
  @Value("${spring.mail.username}")
  private String sender;
  @Override
  public void send(String to, String subject, String content) {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper;
    try {
      messageHelper = new MimeMessageHelper(message, true);
      //邮件发送人
      messageHelper.setFrom(sender);
      //邮件接收人
      messageHelper.setTo(to);
      //邮件主题
      message.setSubject(subject);
      Context context = new Context();
      //设置传入模板的页面的参数 参数名为:id 参数随便写一个就行
      context.setVariable("message", content);
      //emailTemplate是你要发送的模板我这里用的是Thymeleaf
      String process = templateEngine.process("email/email.html", context);
      //邮件内容，html格式
      messageHelper.setText(process, true);
      //发送
      mailSender.send(message);
      log.info("信息发送");
    } catch (Exception e) {
      throw new CustomException("邮箱发送失败");
    }
  }
}
