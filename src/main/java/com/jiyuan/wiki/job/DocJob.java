package com.jiyuan.wiki.job;

import com.jiyuan.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    /**
     * Update e-book information every 30 seconds
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        LOG.info("Start updating the document data under the e-book");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("Updating the document data under the e-book ends, taking: {} milliseconds", System.currentTimeMillis() - start);
    }

}
