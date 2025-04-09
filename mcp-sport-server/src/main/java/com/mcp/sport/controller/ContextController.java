package com.mcp.sport.controller;

import com.mcp.sport.dto.context.ContextRequest;
import com.mcp.sport.dto.context.ContextResponse;
import com.mcp.sport.service.context.ContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for providing rich context about IPL for LLMs and AI agents
 *
 * @author MCP Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/context")
@RequiredArgsConstructor
@Tag(name = "Context API", description = "APIs for providing rich context about IPL")
public class ContextController {

    private final ContextService contextService;

    @PostMapping("/query")
    @Operation(summary = "Query context using standardized format")
    public ResponseEntity<ContextResponse> queryContext(@RequestBody ContextRequest request) {
        return ResponseEntity.ok(contextService.processContextRequest(request));
    }

    @GetMapping("/search")
    @Operation(summary = "Search across all contexts")
    public ResponseEntity<ContextResponse> searchContext(
            @RequestParam String query,
            @RequestParam(required = false) String[] fields,
            @RequestParam(required = false) String[] relationships,
            @RequestParam(required = false, defaultValue = "JSON") String format,
            @RequestParam(required = false, defaultValue = "en") String language,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        ContextRequest request = new ContextRequest();
        request.setContextType("SEARCH");
        request.setContextId(query);
        request.setParameters(Map.of("limit", limit));
        request.setFields(fields);
        request.setRelationships(relationships);
        request.setFormat(format);
        request.setLanguage(language);
        
        return ResponseEntity.ok(contextService.processContextRequest(request));
    }
} 