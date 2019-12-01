package org.simplerss.rssreader.service;

import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;

@CacheConfig(cacheNames={HttpCacheService.CACHE_HTTP})
public class HttpCacheService implements HttpCacheStorage {

    public static final String CACHE_HTTP = "http";

    @Override
    @CachePut
    public void putEntry(String s, HttpCacheEntry httpCacheEntry) throws IOException {

    }

    @Override
    @Cacheable
    public HttpCacheEntry getEntry(String s) throws IOException {
        return null;
    }

    @Override
    @CacheEvict
    public void removeEntry(String s) throws IOException {

    }

    @Override
    @CachePut
    public void updateEntry(String s, HttpCacheUpdateCallback httpCacheUpdateCallback) throws IOException, HttpCacheUpdateException {

    }

}
