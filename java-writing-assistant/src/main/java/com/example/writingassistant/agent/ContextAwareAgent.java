package com.example.writingassistant.agent;

import com.example.writingassistant.service.OpenAIClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContextAwareAgent {
    private final OpenAIClientService openAIClientService;

    public String processWithContext(String prompt, String conversationHistory) {
        String analysisPrompt = String.format("""
            Previous conversation:
            %s
            
            New prompt:
            %s
            
            Please analyze:
            1. How this prompt relates to the previous conversation
            2. Key themes and patterns in the conversation
            3. Suggestions for maintaining consistency
            4. Potential improvements for better flow
            
            Provide a concise analysis that will help improve the response while maintaining context.
            """, conversationHistory, prompt);

        return openAIClientService.generateChatCompletion(
            "You are a context-aware writing assistant that helps maintain conversation flow and coherence.",
            analysisPrompt
        );
    }
} 