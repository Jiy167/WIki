package com.jiyuan.wiki.controller;

import com.jiyuan.wiki.domain.Test;
import com.jiyuan.wiki.service.TestServive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Value("${test.hello:test}")
    private String testHello;

    @Resource
    private TestServive testServive;

    @Resource
    private RedisTemplate redisTemplate;

    //@RequestMapping("/hello")
    @GetMapping("/hello")
    public String hello(){
        return "hello world" + testHello;
    }
    @PostMapping("/hello/post")
    public String hello(String name){
        return "hello world post " + name;
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testServive.list();
    }


    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }
}
