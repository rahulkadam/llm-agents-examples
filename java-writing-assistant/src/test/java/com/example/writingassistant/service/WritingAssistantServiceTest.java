package com.example.writingassistant.service;

import com.example.writingassistant.agent.BasicWritingAgent;
import com.example.writingassistant.agent.ContextAwareAgent;
import com.example.writingassistant.agent.EnhancedWritingAgent;
import com.example.writingassistant.model.ConversationEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WritingAssistantServiceTest {

    @Mock
    private OpenAIClientService openAIClientService;

    private ContextAwareAgent contextAwareAgent;
    private BasicWritingAgent basicWritingAgent;
    private EnhancedWritingAgent enhancedWritingAgent;
    private WritingAssistantService writingAssistantService;

    @BeforeEach
    void setUp() {
        when(openAIClientService.generateChatCompletion(anyString(), anyString()))
            .thenReturn("Mock response");

        contextAwareAgent = new ContextAwareAgent(openAIClientService);
        basicWritingAgent = new BasicWritingAgent(openAIClientService);
        enhancedWritingAgent = new EnhancedWritingAgent(openAIClientService);
        
        writingAssistantService = new WritingAssistantService(
            contextAwareAgent,
            basicWritingAgent,
            enhancedWritingAgent
        );
    }

    @Test
    void processPrompt_ShouldReturnConversationEntry() {
        // Given
        String prompt = "Test prompt";

        // When
        ConversationEntry result = writingAssistantService.processPrompt(prompt);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPrompt()).isEqualTo(prompt);
        assertThat(result.getContextAnalysis()).isEqualTo("Mock response");
        assertThat(result.getImprovements()).isEqualTo("Mock response");
        assertThat(result.getResponse()).isEqualTo("Mock response");
        assertThat(result.getTimestamp()).isNotNull();
    }

    @Test
    void getConversationHistory_ShouldReturnEmptyListInitially() {
        assertThat(writingAssistantService.getConversationHistory()).isEmpty();
    }
} 