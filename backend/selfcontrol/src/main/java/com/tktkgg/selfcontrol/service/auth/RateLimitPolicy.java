package com.tktkgg.selfcontrol.service.auth;

import java.time.Duration;
import java.util.List;

public record RateLimitPolicy(
    int threshold,
    Duration window,
    List<Duration> blockDurations,
    Duration quietPeriod
) {
    public RateLimitPolicy {
        blockDurations = List.copyOf(blockDurations);
    }

    public Duration blockDuration(int stage) {
        int index = Math.min(
            stage,
            blockDurations.size() - 1
        );

        return blockDurations.get(index);
    }
}
