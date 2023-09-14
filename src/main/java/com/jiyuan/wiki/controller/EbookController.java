package com.jiyuan.wiki.controller;

import com.jiyuan.wiki.req.EbookQueryReq;
import com.jiyuan.wiki.req.EbookSaveReq;
import com.jiyuan.wiki.resp.CommonResp;
import com.jiyuan.wiki.resp.EbookQueryResp;
import com.jiyuan.wiki.resp.PageResp;
import com.jiyuan.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        System.out.println("here--------------id: " + id);
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
