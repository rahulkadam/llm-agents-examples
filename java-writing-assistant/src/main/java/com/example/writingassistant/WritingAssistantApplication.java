package com.example.writingassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WritingAssistantApplication {
    public static void main(String[] args) {
        SpringApplication.run(WritingAssistantApplication.class, args);
    }
} 