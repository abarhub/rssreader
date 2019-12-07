package org.simplerss.rssreader.service;

import org.simplerss.rssreader.properties.RssReaderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class SchedulerService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    private SiteRssService siteRssService;
    private RssReaderProperties rssReaderProperties;

    public SchedulerService(SiteRssService siteRssService,
                            RssReaderProperties rssReaderProperties) {
        this.siteRssService = siteRssService;
        this.rssReaderProperties = rssReaderProperties;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.delay}", initialDelayString = "${app.scheduler.initial-delay}")
    public void run(){
        try {
            LOGGER.info("scheduler ...");
            siteRssService.updateRss();
            LOGGER.info("scheduler OK");
        }catch(Exception e){
            LOGGER.error("Erreur pour traiter les sites", e);
        }
    }
}
