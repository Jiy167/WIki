package com.jiyuan.wiki.service;

import com.jiyuan.wiki.mapper.EbookSnapshotMapperCust;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot() {
        ebookSnapshotMapperCust.genSnapshot();
    }

}
