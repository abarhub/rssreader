package org.simplerss.rssreader.utils;

import org.simplerss.rssreader.model.Item;
import org.simplerss.rssreader.model.SiteRss;
import org.simplerss.rssreader.service.SiteRssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class XmlAtomHandler extends DefaultHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(XmlAtomHandler.class);

    public static final String TITLE = "title";
    public static final String RSS = "rss";
    public static final String CHANNEL = "channel";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";
    public static final String PUBDATE = "pubDate";
    public static final String ITEM = "item";

    public static final List<String> LIST_RSS_CHANNEL_TITLE=liste(RSS, CHANNEL, TITLE);
    public static final List<String> LIST_RSS_CHANNEL_DESCRIPTION=liste(RSS, CHANNEL, DESCRIPTION);
    public static final List<String> LIST_RSS_CHANNEL_LINK=liste(RSS, CHANNEL, LINK);
    public static final List<String> LIST_RSS_CHANNEL_PUBDATE=liste(RSS, CHANNEL, PUBDATE);
    public static final List<String> LIST_RSS_CHANNEL_ITEM=liste(RSS, CHANNEL, ITEM);
    public static final List<String> LIST_RSS_CHANNEL_ITEM_TITLE=liste(RSS, CHANNEL, ITEM, TITLE);
    public static final List<String> LIST_RSS_CHANNEL_ITEM_PUBDATE=liste(RSS, CHANNEL, ITEM, PUBDATE);
    public static final List<String> LIST_RSS_CHANNEL_ITEM_DESCRIPTION=liste(RSS, CHANNEL, ITEM, DESCRIPTION);
    public static final List<String> LIST_RSS_CHANNEL_ITEM_LINK=liste(RSS, CHANNEL, ITEM, LINK);

    //private String tagCourant = null;
    private NoeudXml noeudXml;
    private List<String> liste;
    private SiteRss siteRss;
    private Item itemCourant;

    public XmlAtomHandler(NoeudXml noeudXml) {
        this.noeudXml = noeudXml;
        this.liste=new ArrayList<>();
        this.siteRss=new SiteRss();
    }

    public void startElement(String nameSpace, String localName,
                             String qName, Attributes attr) throws SAXException  {
        //tagCourant = qName;
        liste.add(qName);
        if(isChemin(LIST_RSS_CHANNEL_ITEM)) {
            itemCourant = new Item();
            if(siteRss.getItems()==null){
                siteRss.setItems(new ArrayList<>());
            }
            siteRss.getItems().add(itemCourant);
            itemCourant.setSiteRss(siteRss);
        }
    }

    public void endElement(String nameSpace, String localName,
                           String qName) throws SAXException {
        //tagCourant=null;
        if(isChemin(LIST_RSS_CHANNEL_ITEM)) {
            itemCourant = null;
        }
        liste.remove(liste.size()-1);
    }

    public void characters(char[] caracteres, int debut,
                           int longueur) throws SAXException {
        String donnees = new String(caracteres, debut, longueur);
        noeudXml.noeud(new ArrayList<>(liste), donnees);

        if(isBaliseCourrante(TITLE)){
            if(isChemin(LIST_RSS_CHANNEL_TITLE)){
                this.siteRss.setTitre(donnees);
            } else if(isChemin(LIST_RSS_CHANNEL_ITEM_TITLE)){
                itemCourant.setTitre(donnees);
            }
        } else if(isBaliseCourrante(DESCRIPTION)){
            if(isChemin(LIST_RSS_CHANNEL_DESCRIPTION)){
                this.siteRss.setDescription(donnees);
            } else if(isChemin(LIST_RSS_CHANNEL_ITEM_DESCRIPTION)){
                itemCourant.setDescription(donnees);
            }
        } else if(isBaliseCourrante(LINK)){
            if(isChemin(LIST_RSS_CHANNEL_LINK)){
                this.siteRss.setUrl(donnees);
            } else if(isChemin(LIST_RSS_CHANNEL_ITEM_LINK)){
                itemCourant.setUrl(donnees);
            }
        } else if(isBaliseCourrante(PUBDATE)){
            if(isChemin(LIST_RSS_CHANNEL_PUBDATE)){
                this.siteRss.setPublicationDate(parseDate(donnees));
            } else if(isChemin(LIST_RSS_CHANNEL_ITEM_PUBDATE)){
                itemCourant.setPublicationDate(parseDate(donnees));
            }
        }
    }

    private LocalDateTime parseDate(String donnees) {
        if(StringUtils.hasText(donnees)){
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            return LocalDateTime.parse(donnees, formatter);
        } else {
            return null;
        }
    }

    public SiteRss getSiteRss() {
        return siteRss;
    }

    private boolean isBaliseCourrante(String nomBalide){
        if(!liste.isEmpty()){
            if(liste.get(liste.size()-1).equals(nomBalide)){
                return true;
            }
        }
        return false;
    }

    private boolean isChemin(List<String> chemin){
        if(chemin!=null){
            if(liste.equals(chemin)){
                return true;
            }
        }
        return false;
    }

    private static List<String> liste(String... tab){
        List<String> liste=new ArrayList<>();
        if(tab!=null){
            liste.addAll(Arrays.asList(tab));
        }
        return liste;
    }
}
