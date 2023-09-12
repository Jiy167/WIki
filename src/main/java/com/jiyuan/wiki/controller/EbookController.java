package com.jiyuan.wiki.controller;

import com.jiyuan.wiki.domain.Ebook;

import com.jiyuan.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookServive;

    @GetMapping("list1")
    public List<Ebook> list(){
        return ebookServive.list();
    }
}