package com.tktkgg.selfcontrol.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

class RateLimitStateTest {
    private static final Instant BASE_TIME =
        Instant.parse("2026-09-28T00:00:00Z");

    private static final RateLimitPolicy POLICY = new RateLimitPolicy(
        3,
        Duration.ofSeconds(30),
        List.of(
            Duration.ofMinutes(5),
            Duration.ofMinutes(10)
        ),
        Duration.ofHours(24)
    );

    @Test
    void consumeAllowsThresholdAndBlocksTheNextRequest() {
        RateLimitState state = new RateLimitState();

        assertTrue(state.consume(POLICY, BASE_TIME).isEmpty());
        assertTrue(state.consume(POLICY, BASE_TIME.plusSeconds(1)).isEmpty());
        assertTrue(state.consume(POLICY, BASE_TIME.plusSeconds(2)).isEmpty());

        Instant blockedUntil = state.consume(
            POLICY,
            BASE_TIME.plusSeconds(3)
        ).orElseThrow();

        assertEquals(
            BASE_TIME.plusSeconds(3).plus(Duration.ofMinutes(5)),
            blockedUntil
        );
    }

    @Test
    void requestsDuringBlockDoNotExtendTheBlock() {
        RateLimitState state = new RateLimitState();

        for (int i = 0; i < 3; i++) {
            state.consume(POLICY, BASE_TIME.plusSeconds(i));
        }

        Instant blockedUntil = state.consume(
            POLICY,
            BASE_TIME.plusSeconds(3)
        ).orElseThrow();

        Instant blockedUntilAgain = state.consume(
            POLICY,
            BASE_TIME.plusSeconds(4)
        ).orElseThrow();

        assertEquals(blockedUntil, blockedUntilAgain);
    }

    @Test
    void nextBlockUsesTheNextStageAfterTheFirstBlockEnds() {
        RateLimitState state = new RateLimitState();

        for (int i = 0; i < 3; i++) {
            state.consume(POLICY, BASE_TIME.plusSeconds(i));
        }

        Instant firstBlockedUntil = state.consume(
            POLICY,
            BASE_TIME.plusSeconds(3)
        ).orElseThrow();

        Instant nextWindow = firstBlockedUntil.plusSeconds(1);

        for (int i = 0; i < 3; i++) {
            state.consume(POLICY, nextWindow.plusSeconds(i));
        }

        Instant secondBlockedUntil = state.consume(
            POLICY,
            nextWindow.plusSeconds(3)
        ).orElseThrow();

        assertEquals(
            nextWindow.plusSeconds(3).plus(Duration.ofMinutes(10)),
            secondBlockedUntil
        );
    }

    @Test
    void accountFailuresCreateBlockAtTheThreshold() {
        RateLimitState state = new RateLimitState();

        state.recordFailure(POLICY, BASE_TIME);
        state.recordFailure(POLICY, BASE_TIME.plusSeconds(1));
        state.recordFailure(POLICY, BASE_TIME.plusSeconds(2));

        assertTrue(
            state.checkBlocked(
                POLICY,
                BASE_TIME.plusSeconds(3)
            ).isPresent()
        );
    }
}
