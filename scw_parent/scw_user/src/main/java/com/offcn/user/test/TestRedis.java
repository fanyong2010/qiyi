package com.offcn.user.test;


import com.offcn.UserStartApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
// 主类.class
@SpringBootTest(classes = UserStartApp.class)
public class TestRedis {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        redisTemplate.boundValueOps("name").set("森淇");

        stringRedisTemplate.opsForValue().set("key1", "我不过期");
        stringRedisTemplate.opsForValue().set("key2", "我会过期", 10, TimeUnit.MINUTES);

        // System.out.println("添加redis成功...");
        // log.error("我是一个假的error");

        String name = "哈哈，我是变量";

        log.debug("传递的变量是：{}", name);

    }


}
