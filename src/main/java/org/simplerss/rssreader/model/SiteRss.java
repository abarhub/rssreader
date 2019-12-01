package org.simplerss.rssreader.model;

import org.simplerss.rssreader.utils.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Entity
public class SiteRss {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="url", length = Constants.MAX_FIELD, nullable = false)
    private String url;

    @Column(name="titre", length = Constants.MAX_FIELD, nullable = false)
    private String titre;

    @Column(name="description", length = Constants.MAX_FIELD, nullable = false)
    private String description;

    @Column(name="publicationDate", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime publicationDate;

    @OneToMany(mappedBy="siteRss", cascade=CascadeType.ALL)
    private List<Item> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SiteRss.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("url='" + url + "'")
                .add("titre='" + titre + "'")
                .add("description='" + description + "'")
                .add("publicationDate=" + publicationDate)
                .add("items=" + items)
                .toString();
    }
}
