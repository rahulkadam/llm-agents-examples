package com.example.writingassistant.service;

public interface OpenAIClientService {
    String generateChatCompletion(String systemPrompt, String userPrompt);
} 