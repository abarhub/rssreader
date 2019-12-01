package org.simplerss.rssreader.web;

import org.simplerss.rssreader.service.SchedulerService;
import org.simplerss.rssreader.service.SiteRssService;
import org.simplerss.rssreader.web.dto.SiteRssDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SiteRssControler {

    public static final Logger LOGGER = LoggerFactory.getLogger(SiteRssControler.class);

    private SiteRssService siteRssService;

    public SiteRssControler(SiteRssService siteRssService) {
        this.siteRssService=siteRssService;
    }

    @GetMapping("/siteRss")
    public String greetingForm(Model model) {
        model.addAttribute("siteRss", new SiteRssDTO());
        return "siteRss";
    }

    @PostMapping("/siteRss")
    public String siteRssSubmit(@ModelAttribute("siteRss") SiteRssDTO siteRss) {
        LOGGER.info("siteRssSubmit");
        if(siteRss!=null) {
            String url=siteRss.getUrl();
            if(StringUtils.hasText(url)) {
                siteRssService.ajouteSite(url);
            }
        }
        //return "result";
        return "siteRss";
    }


}
