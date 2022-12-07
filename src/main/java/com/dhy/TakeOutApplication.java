package com.dhy;

import com.dhy.controller.ChartController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class TakeOutApplication {

  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(TakeOutApplication.class);
    ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
    ChartController.setApplicationContext(configurableApplicationContext);
  }

}
