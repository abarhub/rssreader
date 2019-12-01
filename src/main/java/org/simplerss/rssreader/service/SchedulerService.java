package org.simplerss.rssreader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
public class SchedulerService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    private SiteRssService siteRssService;

    public SchedulerService(SiteRssService siteRssService) {
        this.siteRssService = siteRssService;
    }

    @Scheduled(fixedDelay = 1000*30, initialDelay = 1000*5)
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
