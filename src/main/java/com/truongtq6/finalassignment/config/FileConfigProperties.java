package com.truongtq6.finalassignment.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Setter@Getter
@AllArgsConstructor@NoArgsConstructor
public class FileConfigProperties {

    private String fileUri;
    private int fileExpiredTime;

}
