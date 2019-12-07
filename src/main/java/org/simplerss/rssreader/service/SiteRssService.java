package org.simplerss.rssreader.service;

import com.google.common.base.Verify;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.simplerss.rssreader.model.Item;
import org.simplerss.rssreader.model.SiteRss;
import org.simplerss.rssreader.repository.SiteRssRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

//@Service
public class SiteRssService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SiteRssService.class);

    private List<String> listeSites;

    private ParseService parseService;

    private SiteRssRepository siteRssRepository;

    private DownloadPageService downloadPageService;

    public SiteRssService(ParseService parseService, SiteRssRepository siteRssRepository, DownloadPageService downloadPageService) {
        listeSites=new CopyOnWriteArrayList<>();
        this.parseService=parseService;
        this.siteRssRepository=siteRssRepository;
        this.downloadPageService=downloadPageService;
    }

    public void ajouteSite(String url){
        if(!StringUtils.isEmpty(url)) {
            LOGGER.info("ajoute du site: {}", url);
            listeSites.add(url);
        }
    }

    private List<String> getCopyListeSites(){
        return new ArrayList<>(listeSites);
    }

    @Transactional
    public void updateRss(){
        LOGGER.info("updateRss");
        List<String> list=getCopyListeSites();
        for(String url:list){
            if(url.startsWith("http")){
                updateSite(url);
            }
        }
    }

    private void updateSite(String url) {
        try {
            Optional<String> contenuOpt=getPage(url);
            if(contenuOpt.isPresent()) {
                String contenu = contenuOpt.get();
                LOGGER.info("page={}", contenu);
                SiteRss siteRssNew = parseService.parse(contenu);

                LOGGER.info("siteRssNew={}", siteRssNew);
                updateDatabse(siteRssNew);
            } else {
                LOGGER.info("rien à recuperer");
            }
        } catch (Exception e) {
            LOGGER.error("Erreur pour mettre à jour le site : {}",url, e);
        }
    }

    private void updateDatabse(SiteRss siteRssNew) {
        Optional<SiteRss> siteRssOptional=siteRssRepository.findByUrl(siteRssNew.getUrl());
        if(siteRssOptional.isPresent()){
            SiteRss siteBase=siteRssOptional.get();
            if(!CollectionUtils.isEmpty(siteRssNew.getItems())){
                int updateItem=0;
                for(Item item:siteRssNew.getItems()){
                    if(item!=null&&item.getUrl()!=null) {
                        Optional<Item> itemOpt = findItem(siteBase, item);
                        if(!itemOpt.isPresent()){
                            updateItem++;
                            LOGGER.info("item ajoute : {}",item);
                            siteBase.getItems().add(item);
                            item.setSiteRss(siteBase);
                        }
                    }
                }
                if(updateItem>0){
                    checkSite(siteRssNew);
                    siteRssRepository.save(siteBase);
                    LOGGER.info("update site (nb={}): {}",updateItem, siteRssNew.getTitre());
                }
            }
        } else {
            checkSite(siteRssNew);
            siteRssRepository.save(siteRssNew);
            LOGGER.info("ajoute site: {}",siteRssNew.getTitre());
        }
    }

    private void checkSite(SiteRss siteRssNew) {
        Verify.verifyNotNull(siteRssNew);
        Verify.verifyNotNull(siteRssNew.getTitre());
        Verify.verifyNotNull(siteRssNew.getDescription());
        Verify.verifyNotNull(siteRssNew.getUrl());
        if(siteRssNew.getItems()!=null){
            for(Item item:siteRssNew.getItems()){
                Verify.verifyNotNull(item.getTitre());
                Verify.verifyNotNull(item.getDescription());
                Verify.verifyNotNull(item.getUrl());
            }
        }
    }

    private Optional<Item> findItem(SiteRss siteBase, Item item) {
        if(item==null){
            return Optional.empty();
        } else if(CollectionUtils.isEmpty(siteBase.getItems())){
            return Optional.empty();
        } else {
            for(Item itemBase:siteBase.getItems()){
                if(itemBase.getUrl()!=null&&item.getUrl()!=null
                        &&itemBase.getUrl().equals(item.getUrl())){
                    return Optional.of(itemBase);
                }
            }
            return Optional.empty();
        }
    }

    private Optional<String> getPage(String url) throws IOException {
        return downloadPageService.getPage(url);
    }

}
