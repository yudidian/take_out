package com.dhy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.Set;

@SpringBootTest
class TakeOutApplicationTests {

  @Autowired
  private RedisTemplate redisTemplate;
  @Test
  void contextLoads() {
    SetOperations setOperations = redisTemplate.opsForSet();
    setOperations.add("MySet", "a", "b", "c");
    Set<String> mySet = setOperations.members("MySet");
    for (String key: mySet) {
      System.out.println(key);
    }
  }

}
