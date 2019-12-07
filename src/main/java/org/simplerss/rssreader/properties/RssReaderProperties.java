package org.simplerss.rssreader.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
public class RssReaderProperties {

    private List<String> siteInitial;

    public List<String> getSiteInitial() {
        return siteInitial;
    }

    public void setSiteInitial(List<String> siteInitial) {
        this.siteInitial = siteInitial;
    }
}
