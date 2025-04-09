package com.mcp.sport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * MCP Sport Server Application
 * 
 * @author MCP Team
 * @since 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class McpSportServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpSportServerApplication.class, args);
    }
} 