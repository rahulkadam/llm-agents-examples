package com.example.writingassistant.agent;

import com.example.writingassistant.service.OpenAIClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasicWritingAgent {
    private final OpenAIClientService openAIClientService;

    public String improveWriting(String prompt, String conversationHistory, String contextAnalysis) {
        String improvementPrompt = String.format("""
            Previous conversation:
            %s
            
            Current prompt:
            %s
            
            Context analysis:
            %s
            
            Please provide improvements while maintaining consistency with previous conversation.
            Focus on clarity, flow, and effectiveness.
            """, conversationHistory, prompt, contextAnalysis);

        return openAIClientService.generateChatCompletion(
            "You are a writing improvement assistant that provides clear, actionable suggestions.",
            improvementPrompt
        );
    }
} 