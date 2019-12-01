package org.simplerss.rssreader.service;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class DownloadPageService {

    public static final Logger LOGGER = LoggerFactory.getLogger(DownloadPageService.class);

    private CloseableHttpClient cachingClient;

    private CloseableHttpClient httpClient;

    private HttpCacheService httpCacheService;

    public DownloadPageService(HttpCacheService httpCacheService) {
        this.httpCacheService = httpCacheService;
    }

    public Optional<String> getPage(String url) throws IOException {
        boolean cache=true;
        if(cache){
            return getPageWithCache(url);
        } else {
            return getPageWithoutCache(url);
        }
    }

    private Optional<String> getPageWithCache(String url) throws IOException {
        CloseableHttpClient cachingClient = initCache();

        HttpCacheContext context = HttpCacheContext.create();
        HttpGet httpget = new HttpGet(url);
        try(CloseableHttpResponse response = cachingClient.execute(httpget, context)){
            CacheResponseStatus responseStatus = context.getCacheResponseStatus();
            switch (responseStatus) {
                case CACHE_HIT:
                    LOGGER.info("A response was generated from the cache with " +
                            "no requests sent upstream");
                    return Optional.empty();
                case CACHE_MODULE_RESPONSE:
                    LOGGER.info("The response was generated directly by the " +
                            "caching module");
                    return Optional.empty();
                case CACHE_MISS:
                    LOGGER.info("The response came from an upstream server");
                    break;
                case VALIDATED:
                    LOGGER.info("The response was generated from the cache " +
                            "after validating the entry with the origin server");
                    return Optional.empty();
            }
            return getContenu(response);
        }
    }

    private CloseableHttpClient initCache() {
        if(cachingClient==null) {
            CacheConfig cacheConfig = CacheConfig.custom()
                    .setMaxCacheEntries(1000)
                    .setMaxObjectSize(8192)
                    .build();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(30000)
                    .setSocketTimeout(30000)
                    .build();
            cachingClient = CachingHttpClients.custom().setHttpCacheStorage(httpCacheService)
                    .setCacheConfig(cacheConfig)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
        return cachingClient;
    }

    private Optional<String> getPageWithoutCache(String url) throws IOException {
        CloseableHttpClient httpclient = getHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try(CloseableHttpResponse response = httpclient.execute(httpGet)) {
            return getContenu(response);
        }
    }

    private Optional<String> getContenu(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        Header encoding = entity.getContentEncoding();
        LOGGER.info("encoding={}", encoding);
        LOGGER.info("contentType={}", entity.getContentType());
        Charset charset = null;
        String contenu = EntityUtils.toString(entity, (charset != null) ? charset : StandardCharsets.UTF_8);
        return Optional.ofNullable(contenu);
    }

    private CloseableHttpClient getHttpClient() {
        if(httpClient==null) {
            httpClient= HttpClients.createDefault();
        }
        return httpClient;
    }
}
