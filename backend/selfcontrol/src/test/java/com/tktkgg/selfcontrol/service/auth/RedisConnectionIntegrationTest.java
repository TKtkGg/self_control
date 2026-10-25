package com.tktkgg.selfcontrol.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;

@SpringBootTest
class RedisConnectionIntegrationTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void canPingRedis() {
        RedisCallback<String> pingCallback =
            connection -> connection.ping();

        String response = redisTemplate.execute(pingCallback);

        assertEquals("PONG", response);
    }
}