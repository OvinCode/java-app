package com.example.dataparse.assignment.controller;
import com.example.dataparse.assignment.service.RedisService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/redis")
@Tag(name = "Redis Operations", description = "Endpoints for managing the redis database")
public class RedisController {

    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisService redisService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        if (!redisService.isRedisConnected()) {
            stats.put("error", "Redis is not connected");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(stats);
        }

        try {
            stats.put("successCount", redisService.getSuccessCount());
            stats.put("failureCount", redisService.getFailureCount());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error retrieving stats: {}", e.getMessage(), e);
            stats.put("error", "Failed to retrieve stats");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(stats);
        }
    }

    @GetMapping("/redis-health")
    public ResponseEntity<String> redisHealthCheck() {
        if (redisService.isRedisConnected()) {
            return ResponseEntity.ok("Redis is connected");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Redis is not connected");
        }
    }
}

