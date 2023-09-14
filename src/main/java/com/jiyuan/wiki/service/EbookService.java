package com.jiyuan.wiki.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.jiyuan.wiki.domain.Ebook;
import com.jiyuan.wiki.domain.EbookExample;
import com.jiyuan.wiki.mapper.EbookMapper;
import com.jiyuan.wiki.req.EbookReq;
import com.jiyuan.wiki.resp.EbookResp;
import com.jiyuan.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;
    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook, ebookResp);
                //object copy
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }

        //listcopy
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return list;

    }
}
