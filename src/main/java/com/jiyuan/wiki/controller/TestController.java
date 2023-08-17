package com.jiyuan.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${test.hello:test}")
    private String testHello;

//    @RequestMapping("/hello")
    @GetMapping("/hello")
    public String hello(){
        return "hello world" + testHello;
    }
    @PostMapping("/hello/post")
    public String hello(String name){
        return "hello world post " + name;
    }
}
