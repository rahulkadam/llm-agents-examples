package com.example.writingassistant.controller;

import com.example.writingassistant.model.ConversationEntry;
import com.example.writingassistant.service.WritingAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingAssistantController {
    private final WritingAssistantService writingAssistantService;

    @PostMapping("/process")
    public ConversationEntry processPrompt(@RequestBody String prompt) {
        return writingAssistantService.processPrompt(prompt);
    }

    @GetMapping("/history")
    public List<ConversationEntry> getHistory() {
        return writingAssistantService.getConversationHistory();
    }
} 