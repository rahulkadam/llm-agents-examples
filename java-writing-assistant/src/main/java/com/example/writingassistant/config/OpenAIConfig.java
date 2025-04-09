package com.example.writingassistant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "openai")
@Data
public class OpenAIConfig {
    private Api api = new Api();

    @Data
    public static class Api {
        private String key;
        private String baseUrl;
        private Integer timeoutSeconds = 30;
    }
} 