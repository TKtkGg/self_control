package com.tktkgg.selfcontrol.service.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import com.tktkgg.selfcontrol.exception.RateLimitExceededException;

class LoginRateLimitServiceTest {
    private static final Instant CURRENT_TIME =
        Instant.parse("2026-09-28T00:00:00Z");

    private LoginRateLimitService service() {
        return new LoginRateLimitService(
            Clock.fixed(CURRENT_TIME, ZoneOffset.UTC)
        );
    }

    @Test
    void ipLimitAllowsFifteenRequestsAndRejectsTheSixteenth() {
        LoginRateLimitService service = service();

        for (int i = 0; i < 15; i++) {
            assertDoesNotThrow(
                () -> service.checkIp("192.0.2.10")
            );
        }

        RateLimitExceededException exception = assertThrows(
            RateLimitExceededException.class,
            () -> service.checkIp("192.0.2.10")
        );

        assertEquals(5 * 60, exception.getRetryAfterSeconds());
    }

    @Test
    void accountLimitIsBasedOnFailuresAndRejectsTheNextAttempt() {
        LoginRateLimitService service = service();
        String accountKey = "user@example.com";

        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(
                () -> service.checkAccount(accountKey)
            );
            service.recordAccountFailure(accountKey);
        }

        RateLimitExceededException exception = assertThrows(
            RateLimitExceededException.class,
            () -> service.checkAccount(accountKey)
        );

        assertEquals(5 * 60, exception.getRetryAfterSeconds());
    }

    @Test
    void successfulLoginResetAllowsTheAccountToStartOver() {
        LoginRateLimitService service = service();
        String accountKey = "user@example.com";

        for (int i = 0; i < 5; i++) {
            service.recordAccountFailure(accountKey);
        }

        assertThrows(
            RateLimitExceededException.class,
            () -> service.checkAccount(accountKey)
        );

        service.resetAccount(accountKey);

        assertDoesNotThrow(
            () -> service.checkAccount(accountKey)
        );
    }
}
