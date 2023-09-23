package com.jiyuan.wiki.job;

import com.jiyuan.wiki.service.EbookSnapshotService;
import com.jiyuan.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @Resource
    private SnowFlake snowFlake;

    /**
     * Customize cron expression to run batch
     * Only after the last execution is completed, the next time will be executed at the next time point. If you miss it, you will miss it.
     */
    @Scheduled(cron = "0 1 * * * ?")
    public void doSnapshot() {
        // Add log serial number
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("Start generating today's e-book snapshot");
        Long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("Generating today's e-book snapshot ends, taking: {} milliseconds", System.currentTimeMillis() - start);
    }

}
