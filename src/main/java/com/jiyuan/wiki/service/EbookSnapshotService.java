package com.jiyuan.wiki.service;

import com.jiyuan.wiki.mapper.EbookSnapshotMapperCust;
import com.jiyuan.wiki.resp.StatisticResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot() {
        ebookSnapshotMapperCust.genSnapshot();
    }

    /**
     * Obtain home page numerical data: total readings, total likes, today's readings, today's likes, today's estimated readings, today's estimated reading growth
     */
    public List<StatisticResp> getStatistic() {
        return ebookSnapshotMapperCust.getStatistic();
    }

    /**
     *30 days numerical statistics
     */
    public List<StatisticResp> get30Statistic() {
        return ebookSnapshotMapperCust.get30Statistic();
    }

}
