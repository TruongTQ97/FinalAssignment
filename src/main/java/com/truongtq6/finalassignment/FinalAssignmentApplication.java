package com.truongtq6.finalassignment;

import com.truongtq6.finalassignment.config.FileConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileConfigProperties.class)
public class FinalAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalAssignmentApplication.class, args);
    }

}
