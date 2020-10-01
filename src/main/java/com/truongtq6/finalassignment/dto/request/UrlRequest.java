package com.truongtq6.finalassignment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor@NoArgsConstructor
@Data
@Builder
@XmlRootElement
public class UrlRequest {

    private String username;
    private String originalUrl;
}
