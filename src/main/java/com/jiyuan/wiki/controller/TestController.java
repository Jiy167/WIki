package com.jiyuan.wiki.controller;

import com.jiyuan.wiki.domain.Test;
import com.jiyuan.wiki.service.TestServive;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Value("${test.hello:test}")
    private String testHello;

    @Resource
    private TestServive testServive;

//    @RequestMapping("/hello")
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
}
