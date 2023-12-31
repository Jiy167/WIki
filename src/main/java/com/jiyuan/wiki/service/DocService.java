package com.jiyuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiyuan.wiki.domain.Content;
import com.jiyuan.wiki.domain.Doc;
import com.jiyuan.wiki.domain.DocExample;
import com.jiyuan.wiki.exception.BusinessException;
import com.jiyuan.wiki.exception.BusinessExceptionCode;
import com.jiyuan.wiki.mapper.ContentMapper;
import com.jiyuan.wiki.mapper.DocMapper;
import com.jiyuan.wiki.mapper.DocMapperCust;
import com.jiyuan.wiki.req.DocQueryReq;
import com.jiyuan.wiki.req.DocSaveReq;
import com.jiyuan.wiki.resp.DocQueryResp;
import com.jiyuan.wiki.resp.PageResp;
import com.jiyuan.wiki.util.CopyUtil;
import com.jiyuan.wiki.util.RedisUtil;
import com.jiyuan.wiki.util.RequestContext;
import com.jiyuan.wiki.util.SnowFlake;
import com.jiyuan.wiki.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    public RedisUtil redisUtil;

    @Resource
    public WsService wsService;

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        // list copy
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("total row：{}", pageInfo.getTotal());
        LOG.info("total page：{}", pageInfo.getPages());

        // List<DocResp> respList = new ArrayList<>();
        // for (Doc doc : docList) {
        //     // DocResp docResp = new DocResp();
        //     // BeanUtils.copyProperties(doc, docResp);
        //     // object copy
        //     DocResp docResp = CopyUtil.copy(doc, DocResp.class);
        //
        //     respList.add(docResp);
        // }

        // list copy
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * save
     */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // add
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            // update
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    /**
     * likes
     */
    public void vote(Long id) {
        // docMapperCust.increaseVoteCount(id);
        // Remote IP+doc.id is used as key and cannot be repeated within 24 hours.
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        // forward news
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("[" + docDb.getName() + "] is Liked!", logId);
    }

    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }

}
