package com.truongtq6.finalassignment.repository;

import com.truongtq6.finalassignment.entity.URLEntity;
import org.springframework.data.repository.CrudRepository;

public interface UrlShortenerRepository extends CrudRepository<URLEntity, Long> {

    URLEntity findByShortenURL(String shortenUrl);
}
