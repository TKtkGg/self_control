package com.tktkgg.selfcontrol.service.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class RateLimitState {
    private int count;
    private int stage;

    private Instant windowStartedAt;
    private Instant blockedUntil;
    private Instant stageResetAt;

    public synchronized Optional<Instant> consume(
        RateLimitPolicy policy,
        Instant now
    ) {
        prepare(policy, now);

        if (isBlocked(now)) {
            return Optional.of(blockedUntil);
        }

        resetWindowIfNeeded(policy, now);
        count++;

        if (count > policy.threshold()) {
            return startBlock(policy, now);
        }

        return Optional.empty();
    }

    public synchronized Optional<Instant> checkBlocked(
        RateLimitPolicy policy,
        Instant now
    ) {
        prepare(policy, now);

        if (isBlocked(now)) {
            return Optional.of(blockedUntil);
        }

        return Optional.empty();
    }

    public synchronized void recordFailure(
        RateLimitPolicy policy,
        Instant now
    ) {
        prepare(policy, now);

        if (isBlocked(now)) {
            return;
        }

        resetWindowIfNeeded(policy, now);
        count++;

        if (count >= policy.threshold()) {
            startBlock(policy, now);
        }
    }

    private void prepare(
        RateLimitPolicy policy,
        Instant now
    ) {
        if (stageResetAt != null && !now.isBefore(stageResetAt)) {
            count = 0;
            stage = 0;
            windowStartedAt = null;
            stageResetAt = null;
        }

        if (blockedUntil != null && !now.isBefore(blockedUntil)) {
            blockedUntil = null;
        }
    }

    private boolean isBlocked(Instant now) {
        return blockedUntil != null
            && now.isBefore(blockedUntil);
    }

    private void resetWindowIfNeeded(
        RateLimitPolicy policy,
        Instant now
    ) {
        if (
            windowStartedAt == null
            || !now.isBefore(windowStartedAt.plus(policy.window()))
        ) {
            count = 0;
            windowStartedAt = now;
        }
    }

    private Optional<Instant> startBlock(
        RateLimitPolicy policy, 
        Instant now
    ) {
        Duration blockDuration = policy.blockDuration(stage);

        blockedUntil = now.plus(blockDuration);

        stageResetAt = blockedUntil.plus(policy.quietPeriod());

        stage = Math.min(stage + 1, policy.blockDurations().size() - 1);

        count = 0;
        windowStartedAt = null;

        return Optional.of(blockedUntil);
    }
}
