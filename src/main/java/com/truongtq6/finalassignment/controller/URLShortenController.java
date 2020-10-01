package com.truongtq6.finalassignment.controller;

import com.truongtq6.finalassignment.dto.SharedFileDTO;
import com.truongtq6.finalassignment.dto.request.UrlRequest;
import com.truongtq6.finalassignment.dto.response.FileResponse;
import com.truongtq6.finalassignment.service.FileService;
import com.truongtq6.finalassignment.service.UrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@ControllerAdvice
@AllArgsConstructor
public class URLShortenController {

    private  UrlShortenerService urlShortenerService;
    private FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity redirect(@PathVariable String id, HttpServletRequest request, HttpServletResponse resp) throws Exception {
        String originalUri = urlShortenerService.findOriginalUrlByShortenedUrl(id);

        if(originalUri != null){
            resp.sendRedirect(originalUri);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/shortenURL")
    public String save(@RequestParam("filename") String filename, HttpServletRequest request, Model model) {
        User logUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FileResponse fileResponse = fileService.findFileByUsernameAndFilename(logUser.getUsername(), filename);

        String baseURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/").build().toUriString();

        String shortenedURL = urlShortenerService.shortenURL(UrlRequest.builder().username(logUser.getUsername()).originalUrl(fileResponse.getDownloadUri()).build(), request);

        if (shortenedURL != null) {
                model.addAttribute("shortenUrl", baseURL + shortenedURL);
                model.addAttribute("filename", fileResponse.getFileName());
                model.addAttribute("sharedFileDTO", new SharedFileDTO());
        }

        return "sharefile";
    }

}
