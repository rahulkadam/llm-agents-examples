package com.example.writingassistant.agent;

import com.example.writingassistant.service.OpenAIClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnhancedWritingAgent {
    private final OpenAIClientService openAIClientService;

    public String enhanceWriting(String prompt, String conversationHistory, String contextAnalysis, String basicImprovements) {
        String enhancementPrompt = String.format("""
            Conversation history:
            %s
            
            Current prompt:
            %s
            
            Context analysis:
            %s
            
            Basic improvements:
            %s
            
            Please provide enhanced improvements while maintaining conversation flow.
            Focus on:
            1. Clarity and impact
            2. Natural flow with context
            3. Specific word choices
            4. Overall effectiveness
            """, conversationHistory, prompt, contextAnalysis, basicImprovements);

        return openAIClientService.generateChatCompletion(
            "You are an advanced writing enhancement agent that provides deep, thoughtful improvements while maintaining the author's intent.",
            enhancementPrompt
        );
    }
} 