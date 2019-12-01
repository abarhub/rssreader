package org.simplerss.rssreader.model;

import org.simplerss.rssreader.utils.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="titre", length = Constants.MAX_FIELD, nullable = true)
    private String titre;

    @Column(name="description", length = Constants.MAX_FIELD, nullable = true)
    private String description;

    @Column(name="url", length = Constants.MAX_FIELD, nullable = true)
    private String url;

    @Column(name="publicationDate", nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime publicationDate;

    @ManyToOne
    private SiteRss siteRss;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SiteRss getSiteRss() {
        return siteRss;
    }

    public void setSiteRss(SiteRss siteRss) {
        this.siteRss = siteRss;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Item.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("titre='" + titre + "'")
                .add("description='" + description + "'")
                .add("url='" + url + "'")
                .add("publicationDate=" + publicationDate)
                .toString();
    }
}
