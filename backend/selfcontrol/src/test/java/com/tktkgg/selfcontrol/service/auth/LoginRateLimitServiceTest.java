package com.tktkgg.selfcontrol.service.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.tktkgg.selfcontrol.exception.RateLimitExceededException;

@SpringBootTest 
class LoginRateLimitServiceTest {
    private static final String IP_KEY_PREFIX =
        "self-control:login-rate-limit-ip:";

    private static final String ACCOUNT_KEY_PREFIX =
        "self-control:login-rate-limit:account:";

    @Autowired 
    private LoginRateLimitService service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void ipLimitAllowsFifteenRequestsAndRejectsTheSixteenth() {
        String ipAddress = "rate-limit-test-" + UUID.randomUUID();

        try {
            for (int i = 0; i < 15; i++) {
                assertDoesNotThrow(() -> service.checkIp(ipAddress));
            }

            RateLimitExceededException exception = assertThrows(
                RateLimitExceededException.class, 
                () -> service.checkIp(ipAddress)
            );

            assertTrue(
                exception.getRetryAfterSeconds() > 0
                && exception.getRetryAfterSeconds() <= 5 * 60
            );
        } finally {
            redisTemplate.delete(IP_KEY_PREFIX + ipAddress);
        }
    }

    @Test
    void accountLimitBlocksAfterFiveFailures() {
        String accountKey = "rate-limit-test-" + UUID.randomUUID();

        try {
            for (int i = 0; i < 5; i++) {
                assertDoesNotThrow(() -> service.checkAccount(accountKey));
                service.recordAccountFailure(accountKey);
            }

            RateLimitExceededException exception = assertThrows(
                RateLimitExceededException.class,
                () -> service.checkAccount(accountKey)
            );

            assertTrue(
                exception.getRetryAfterSeconds() > 0
                    && exception.getRetryAfterSeconds() <= 5 * 60
            );
        } finally {
            redisTemplate.delete(ACCOUNT_KEY_PREFIX + accountKey);
        }
    }

    @Test
    void successfulLoginResetRemovesAccountLimit() {
        String accountKey = "rate-limit-test-" + UUID.randomUUID();

        try {
            for (int i = 0; i < 5; i++) {
                service.recordAccountFailure(accountKey);
            }

            assertThrows(
                RateLimitExceededException.class,
                () -> service.checkAccount(accountKey)
            );

            service.resetAccount(accountKey);

            assertDoesNotThrow(() -> service.checkAccount(accountKey));
        } finally {
            redisTemplate.delete(ACCOUNT_KEY_PREFIX + accountKey);
        }
    }
}
