package com.jiyuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiyuan.wiki.domain.Ebook;
import com.jiyuan.wiki.domain.EbookExample;
import com.jiyuan.wiki.mapper.EbookMapper;
import com.jiyuan.wiki.req.EbookQueryReq;
import com.jiyuan.wiki.req.EbookSaveReq;
import com.jiyuan.wiki.resp.EbookQueryResp;
import com.jiyuan.wiki.resp.PageResp;
import com.jiyuan.wiki.util.CopyUtil;
import com.jiyuan.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;


    public PageResp<EbookQueryResp> list(EbookQueryReq req){

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("total row：{}", pageInfo.getTotal());
        LOG.info("total page：{}", pageInfo.getPages());

//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook, ebookResp);
                //object copy
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        //listcopy
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;

    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // add
            ebook.setId(snowFlake.nextId());
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            ebookMapper.insert(ebook);
        } else {
            // update
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }


    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }
}
