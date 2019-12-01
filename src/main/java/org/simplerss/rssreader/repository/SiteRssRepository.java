package org.simplerss.rssreader.repository;

import org.simplerss.rssreader.model.SiteRss;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SiteRssRepository extends CrudRepository<SiteRss, Long> {

    Optional<SiteRss> findByUrl(String url);
}
