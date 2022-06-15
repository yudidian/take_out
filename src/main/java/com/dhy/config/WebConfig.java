package com.dhy.config;


import com.dhy.common.JacksonObjectMapper;
import com.dhy.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TokenInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/employee/login")
        .excludePathPatterns("/email/**")
        .excludePathPatterns("/user/login")
        .excludePathPatterns("/download");
  }

  public void extendMessageConverters(List<HttpMessageConverter<?>> converterList){
    MappingJackson2HttpMessageConverter messageConverter =new MappingJackson2HttpMessageConverter();
    messageConverter.setObjectMapper(new JacksonObjectMapper());
    converterList.add(0,messageConverter);
   }
}