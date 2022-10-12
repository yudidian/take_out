package com.dhy.config;


import com.dhy.common.JacksonObjectMapper;
import com.dhy.interceptor.TokenInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
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
  /**
   *添加消息转化类
   * @param list
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = jsonConverter.getObjectMapper();
    //序列换成json时,将所有的long变成string
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
    objectMapper.registerModule(simpleModule);
    list.add(jsonConverter);
  }
}