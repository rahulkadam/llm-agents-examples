package com.example.writingassistant.service;

import com.example.writingassistant.agent.BasicWritingAgent;
import com.example.writingassistant.agent.ContextAwareAgent;
import com.example.writingassistant.agent.EnhancedWritingAgent;
import com.example.writingassistant.model.ConversationEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WritingAssistantService {
    private final ContextAwareAgent contextAwareAgent;
    private final BasicWritingAgent basicWritingAgent;
    private final EnhancedWritingAgent enhancedWritingAgent;
    
    private final List<ConversationEntry> conversationHistory = new ArrayList<>();

    public ConversationEntry processPrompt(String prompt) {
        String previousConversation = formatPreviousConversation();
        
        // Step 1: Analyze context
        String contextAnalysis = contextAwareAgent.processWithContext(prompt, previousConversation);
        
        // Step 2: Get basic improvements
        String basicImprovements = basicWritingAgent.improveWriting(prompt, previousConversation, contextAnalysis);
        
        // Step 3: Get enhanced improvements
        String enhancedResponse = enhancedWritingAgent.enhanceWriting(
            prompt, 
            previousConversation, 
            contextAnalysis, 
            basicImprovements
        );

        // Create and store conversation entry
        ConversationEntry entry = ConversationEntry.builder()
            .prompt(prompt)
            .contextAnalysis(contextAnalysis)
            .improvements(basicImprovements)
            .response(enhancedResponse)
            .timestamp(LocalDateTime.now())
            .build();

        conversationHistory.add(entry);
        return entry;
    }

    private String formatPreviousConversation() {
        if (conversationHistory.isEmpty()) {
            return "";
        }

        return conversationHistory.stream()
            .map(entry -> String.format("""
                Prompt: %s
                Response: %s
                ---
                """, entry.getPrompt(), entry.getResponse()))
            .collect(Collectors.joining("\n"));
    }

    public List<ConversationEntry> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }
} 