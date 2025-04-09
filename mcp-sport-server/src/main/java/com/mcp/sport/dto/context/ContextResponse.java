package com.mcp.sport.dto.context;

import lombok.Data;
import java.util.Map;

/**
 * Standardized response format for context API
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Data
public class ContextResponse {
    private String contextType;  // e.g., "TEAM", "PLAYER", "MATCH", "SEASON"
    private String contextId;    // identifier of the context
    private Map<String, Object> data;
    private Map<String, Object> metadata;
    private Map<String, Object> relationships;
    private Map<String, Object> statistics;
    private String naturalLanguageSummary;
    private String[] suggestedQueries;
    private String[] relatedContexts;
} 