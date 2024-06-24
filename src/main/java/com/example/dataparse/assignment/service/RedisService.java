package com.example.dataparse.assignment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private static final String SUCCESS_KEY = "SUCCESS";
    private static final String FAILURE_KEY = "FAILURE";

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public void incrementSuccessCounter() {
        try {
            Long newValue = redisTemplate.opsForValue().increment(SUCCESS_KEY);
            logger.info("Incremented success counter. New value: {}", newValue);
        } catch (Exception e) {
            logger.error("Failed to increment success counter: {}", e.getMessage(), e);
        }
    }

    public void incrementFailureCounter() {
        try {
            Long newValue = redisTemplate.opsForValue().increment(FAILURE_KEY);
            logger.info("Incremented failure counter. New value: {}", newValue);
        } catch (Exception e) {
            logger.error("Failed to increment failure counter: {}", e.getMessage(), e);
        }
    }

    public Long getSuccessCount() {
        try {

            Long value = Long.parseLong(String.valueOf(redisTemplate.opsForValue().get(SUCCESS_KEY)));
            return value;
        } catch (Exception e) {
            logger.error("Failed to get success count: {}", e.getMessage(), e);
            return 0L;
        }
    }

    public Long getFailureCount() {
        try {
            Long value = Long.parseLong(String.valueOf(redisTemplate.opsForValue().get(FAILURE_KEY)));
            return value;
        } catch (Exception e) {
            logger.error("Failed to get failure count: {}", e.getMessage(), e);
            return 0L;
        }
    }


    public boolean isRedisConnected() {
        try {
            String result = redisTemplate.getConnectionFactory().getConnection().ping();
            return "PONG".equals(result);
        } catch (Exception e) {
            logger.error("Redis connection test failed", e);
            return false;
        }
    }
}