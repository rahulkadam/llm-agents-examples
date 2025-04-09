package com.example.writingassistant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationEntry {
    private String prompt;
    private String contextAnalysis;
    private String improvements;
    private String response;
    private LocalDateTime timestamp;
} 