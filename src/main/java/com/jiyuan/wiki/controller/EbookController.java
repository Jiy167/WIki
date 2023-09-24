package com.jiyuan.wiki.controller;

import com.jiyuan.wiki.req.EbookQueryReq;
import com.jiyuan.wiki.req.EbookSaveReq;
import com.jiyuan.wiki.resp.CommonResp;
import com.jiyuan.wiki.resp.EbookQueryResp;
import com.jiyuan.wiki.resp.PageResp;
import com.jiyuan.wiki.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    private static final Logger LOG = LoggerFactory.getLogger(EbookController.class);

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody  EbookSaveReq req){
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

    @RequestMapping("/upload/avatar")
    public CommonResp upload(@RequestParam MultipartFile avatar) throws IOException {
        LOG.info("Start of uploading file: {}", avatar);
        LOG.info("File name: {}", avatar.getOriginalFilename());
        LOG.info("File size: {}", avatar.getSize());

        //Save the file locally
        String fileName = avatar.getOriginalFilename();
        String fullPath = "D:/file/wiki/" + fileName;
        File dest = new File(fullPath);
        avatar.transferTo(dest);
        LOG.info(dest.getAbsolutePath());

        return new CommonResp();
    }
}
