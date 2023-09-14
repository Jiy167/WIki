package com.jiyuan.wiki.controller;

import com.jiyuan.wiki.domain.Ebook;

import com.jiyuan.wiki.req.EbookReq;
import com.jiyuan.wiki.resp.CommonResp;
import com.jiyuan.wiki.resp.EbookResp;
import com.jiyuan.wiki.resp.PageResp;
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

    @GetMapping("list")
    public CommonResp list(EbookReq req){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookServive.list(req);
        resp.setContent(list);
        return resp;
    }
}
