package com.example.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.api")
@Data
public class AppConfig {
    private String hostUrl;
    private String appid;
    private String apiSecret;
    private String apiKey;
}
