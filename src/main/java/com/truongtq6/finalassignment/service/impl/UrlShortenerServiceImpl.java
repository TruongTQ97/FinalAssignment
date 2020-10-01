package com.truongtq6.finalassignment.service.impl;

import com.google.common.hash.Hashing;
import com.truongtq6.finalassignment.dto.request.UrlRequest;
import com.truongtq6.finalassignment.entity.FileEntity;
import com.truongtq6.finalassignment.entity.URLEntity;
import com.truongtq6.finalassignment.repository.FileRepository;
import com.truongtq6.finalassignment.repository.UrlShortenerRepository;
import com.truongtq6.finalassignment.service.UrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlShortenerRepository repository;
    private final FileRepository fileRepository;

    public String getShortenURL(UrlRequest urlRequest, HttpServletRequest request) {
            final String id = Hashing.murmur3_32().hashString(urlRequest.getUsername() + Instant.now().toEpochMilli(), StandardCharsets.UTF_8).toString();
            return request.getServletContext().getContextPath() + id;
    }

    @Override
    public URLEntity saveURL(URLEntity entity) {
        return repository.save(entity);
    }

    @Override
    public String findOriginalUrlByShortenedUrl(String shortenedUrl) {
        URLEntity entity = repository.findByShortenURL(shortenedUrl);
        return entity != null ? entity.getOriginalFile().getDownloadUri() : null;
    }

    @Override
    public String shortenURL(UrlRequest urlRequest, HttpServletRequest request){
        String originalURL = urlRequest.getOriginalUrl();
        FileEntity fileEntity = fileRepository.findByDownloadUri(originalURL);
        if(fileEntity != null){
            URLEntity detachedEntity = URLEntity.builder()
                    .originalFile(fileEntity).createdAt(LocalDateTime.now()).expiredDate(LocalDateTime.now().plusHours(24)).shortenURL(getShortenURL(urlRequest, request))
                    .build();
            return saveURL(detachedEntity).getShortenURL();
        }
        return null;
    }
}
