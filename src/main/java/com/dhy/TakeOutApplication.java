package com.dhy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TakeOutApplication {

  public static void main(String[] args) {
    SpringApplication.run(TakeOutApplication.class, args);
  }

}
