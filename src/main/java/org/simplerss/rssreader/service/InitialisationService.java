package org.simplerss.rssreader.service;

import org.simplerss.rssreader.properties.RssReaderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

public class InitialisationService {

    public static final Logger LOGGER = LoggerFactory.getLogger(InitialisationService.class);

    private SiteRssService siteRssService;
    private RssReaderProperties rssReaderProperties;

    public InitialisationService(SiteRssService siteRssService, RssReaderProperties rssReaderProperties) {
        this.siteRssService = siteRssService;
        this.rssReaderProperties = rssReaderProperties;
    }

    @PostConstruct
    public void init(){
        List<String> listeSites=rssReaderProperties.getSiteInitial();
        LOGGER.info("listeSites={}", listeSites);
        if(!CollectionUtils.isEmpty(listeSites)) {
            for(String url:rssReaderProperties.getSiteInitial()) {
                siteRssService.ajouteSite(url);
            }
        }
    }
}
