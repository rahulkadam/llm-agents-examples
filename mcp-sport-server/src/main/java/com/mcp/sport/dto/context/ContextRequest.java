package com.mcp.sport.dto.context;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

/**
 * Standardized request format for context API
 *
 * @author MCP Team
 * @since 1.0.0
 */
@Data
public class ContextRequest {
    @NotBlank(message = "Context type is required")
    private String contextType;  // e.g., "TEAM", "PLAYER", "MATCH", "SEASON"

    @NotBlank(message = "Context ID is required")
    private String contextId;    // identifier of the context

    private Map<String, Object> parameters;
    private String[] fields;     // specific fields to include in response
    private String[] relationships; // specific relationships to include

    @NotNull(message = "Format is required")
    private String format = "JSON";       // response format (e.g., "JSON", "NATURAL_LANGUAGE")

    @NotNull(message = "Language is required")
    private String language = "en";     // preferred language for natural language responses
} 