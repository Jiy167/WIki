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
    private EbookService ebookServive;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookServive.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookServive.save(req);
        return resp;
    }
}
