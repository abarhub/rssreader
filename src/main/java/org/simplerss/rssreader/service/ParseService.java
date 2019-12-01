package org.simplerss.rssreader.service;

import org.simplerss.rssreader.model.SiteRss;
import org.simplerss.rssreader.utils.XmlAtomHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class ParseService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ParseService.class);

    public SiteRss parse(String contenu){

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();

            final XmlAtomHandler xmlAtomHandler = new XmlAtomHandler(this::noeud);
            parser.parse(new InputSource(new StringReader(contenu)), xmlAtomHandler);

            return xmlAtomHandler.getSiteRss();
        } catch (SAXParseException e) {
            LOGGER.error("Erreur pour parser le fichier à la position {}/{} : {}",
                    e.getLineNumber(),e.getColumnNumber(), e.getMessage());
            return null;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.error("Erreur pour parser le fichier : {}",e.getMessage());
            return null;
        }
    }

    private void noeud(List<String> balises, String valeur){
        LOGGER.debug("balises={}; valeur={}", balises, valeur);
    }
}
