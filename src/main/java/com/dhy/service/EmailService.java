package com.dhy.service;

import com.dhy.DTO.EmailDto;

public interface EmailService {
  void send(String to, String subject, String content);
}
