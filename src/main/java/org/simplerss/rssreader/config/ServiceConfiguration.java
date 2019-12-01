package org.simplerss.rssreader.config;

import org.simplerss.rssreader.repository.SiteRssRepository;
import org.simplerss.rssreader.service.*;
import org.simplerss.rssreader.web.SiteRssControler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ServiceConfiguration {

//    @Bean
//    public SiteRssControler siteRssControler(SiteRssService siteRssService){
//        return new SiteRssControler(siteRssService);
//    }

    @Bean
    public SchedulerService schedulerService(SiteRssService siteRssService){
        return new SchedulerService(siteRssService);
    }

    @Bean
    public SiteRssService siteRssService(ParseService parseService, SiteRssRepository siteRssRepository,
                                         DownloadPageService downloadPageService){
        return new SiteRssService(parseService, siteRssRepository, downloadPageService);
    }

    @Bean
    public ParseService parseService(){
        return new ParseService();
    }

    @Bean
    public DownloadPageService downloadPageService(HttpCacheService httpCacheService){
        return new DownloadPageService(httpCacheService);
    }
}
