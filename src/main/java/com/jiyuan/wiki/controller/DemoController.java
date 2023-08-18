package com.jiyuan.wiki.controller;

import com.jiyuan.wiki.domain.Demo;
import com.jiyuan.wiki.service.DemoServive;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private DemoServive demoServive;

    @GetMapping("list1")
    public List<Demo> list(){
        return demoServive.list();
    }
}
