package com.truongtq6.finalassignment.service;

import com.truongtq6.finalassignment.dto.request.UrlRequest;
import com.truongtq6.finalassignment.entity.URLEntity;

import javax.servlet.http.HttpServletRequest;

public interface UrlShortenerService {

    String shortenURL(UrlRequest urlRequest, HttpServletRequest request);
    URLEntity saveURL(URLEntity entity);
    String findOriginalUrlByShortenedUrl(String shortenedUrl);
}
