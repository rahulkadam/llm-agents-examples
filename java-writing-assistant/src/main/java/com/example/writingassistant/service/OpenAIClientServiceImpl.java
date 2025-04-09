package com.example.writingassistant.service;

import com.example.writingassistant.config.OpenAIConfig;
import com.openai.client.OpenAIClient;
import com.openai.credential.BearerTokenCredential;
import org.springframework.stereotype.Service;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

@Service
public class OpenAIClientServiceImpl implements OpenAIClientService {
    private final OpenAIClient client;

    public OpenAIClientServiceImpl(OpenAIConfig config) {
        OpenAIConfig.Api apiConfig = config.getApi();
        
        client = OpenAIOkHttpClient.builder()
            .baseUrl(apiConfig.getBaseUrl())
            .credential(apiConfig.getKey())
            .build();
    }

    @Override
    public String generateChatCompletion(String systemPrompt, String userPrompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(userPrompt)
                .addSystemMessage(systemPrompt)
                .model(ChatModel.GPT_3_5_TURBO)  // Changed to GPT-3.5-turbo
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);

        return chatCompletion.choices().get(0).message().toString();
    }
} 