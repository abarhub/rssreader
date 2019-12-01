package org.simplerss.rssreader.config;

import org.simplerss.rssreader.service.HttpCacheService;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@EnableCaching
@Configuration
public class CachingConfig {

    @Bean
    public HttpCacheService httpCacheService(){
        return new HttpCacheService();
    }
}
