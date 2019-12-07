package org.simplerss.rssreader.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

//@CacheConfig(cacheNames={HttpCacheService.CACHE_HTTP})
public class HttpCacheService implements HttpCacheStorage {

    public static final Logger LOGGER = LoggerFactory.getLogger(HttpCacheService.class);

    public static final String CACHE_HTTP = "http";

    private Cache<String, HttpCacheEntry> cache;

    public HttpCacheService() {
        cache = Caffeine.newBuilder()
                //.expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
    }

    @Override
    //@CachePut
    public void putEntry(String s, HttpCacheEntry httpCacheEntry) throws IOException {
        LOGGER.info("putEntry({})",s);
        logStat();
        cache.put(s,httpCacheEntry);
        logStat2();
    }

    @Override
    //@Cacheable
    public HttpCacheEntry getEntry(String s) throws IOException {
        LOGGER.info("getEntry({})",s);
        logStat();
        @Nullable HttpCacheEntry res = cache.getIfPresent(s);
        logStat2();
        return res;
    }

    @Override
    //@CacheEvict
    public void removeEntry(String s) throws IOException {
        LOGGER.info("removeEntry({})",s);
        logStat();
        cache.invalidate(s);
        logStat2();
    }

    @Override
    //@CachePut
    public void updateEntry(String s, HttpCacheUpdateCallback httpCacheUpdateCallback) throws IOException, HttpCacheUpdateException {
        LOGGER.info("updateEntry({})",s);
        logStat();
        @Nullable HttpCacheEntry res = cache.getIfPresent(s);
        if(res!=null) {
            res=httpCacheUpdateCallback.update(res);
            cache.put(s, res);
        }
        logStat2();
    }

    private void logStat() {
        LOGGER.info("stat cache={}", cache.stats());
    }

    private void logStat2() {
        LOGGER.info("object cache={}", cache);
        LOGGER.info("stat cache2={}", cache.stats());
        LOGGER.info("cache={}", cache.asMap());
    }

}
