package com.mcp.sport.config;

import com.mcp.sport.service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final DataLoaderService dataLoaderService;

    @Override
    public void run(String... args) throws Exception {
        dataLoaderService.loadData();
    }
} 