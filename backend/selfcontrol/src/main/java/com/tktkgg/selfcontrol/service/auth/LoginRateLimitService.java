package com.tktkgg.selfcontrol.service.auth;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.exception.RateLimitExceededException;

@Service 
public class LoginRateLimitService {
    private static final RateLimitPolicy IP_POLICY = new RateLimitPolicy(
        15,
        Duration.ofMinutes(1),
        List.of(
            Duration.ofMinutes(5),
            Duration.ofMinutes(10),
            Duration.ofMinutes(30)
        ), 
        Duration.ofHours(24)
    );

    private static final RateLimitPolicy ACCOUNT_POLICY = new RateLimitPolicy(
        5,
        Duration.ofSeconds(30), 
        List.of(
            Duration.ofMinutes(5),
            Duration.ofMinutes(15),
            Duration.ofHours(1),
            Duration.ofHours(6)
        ),
        Duration.ofHours(24)
    );

    private final Clock clock;

    private final ConcurrentMap<String, RateLimitState> states = 
        new ConcurrentHashMap<>();

    public LoginRateLimitService(Clock clock) {
        this.clock = clock;
    }

    public void checkIp(String ipAddress) {
        String key = "ip:" + ipAddress;

        Optional<Instant> blockedUntil = getState(key)
            .consume(IP_POLICY, clock.instant());
        
        rejectIfBlocked(blockedUntil);
    }

    public void checkAccount(String accountKey) {
        String key = "account:" + accountKey;

        Optional<Instant> blockedUntil = getState(key)
            .checkBlocked(ACCOUNT_POLICY, clock.instant());
        
        rejectIfBlocked(blockedUntil);
    }

    public void resetAccountFailure(String accountKey) {
        String key = "account:" + accountKey;

        getState(key).recordFailure(ACCOUNT_POLICY, clock.instant());
    }

    public void resetAccount(String accountKey) {
        states.remove("account:" + accountKey);
    }

    private RateLimitState getState(String key) {
        return states.computeIfAbsent(key, ignored -> new RateLimitState());
    }

    private void rejectIfBlocked(Optional<Instant> blockedUntil) {
        if (blockedUntil.isEmpty()) {
            return;
        }

        long retryAfterSeconds = Math.max(
            1, 
            Duration.between(
                clock.instant(), 
                blockedUntil.get()
            ).toSeconds()
        );

        throw new RateLimitExceededException(retryAfterSeconds);
    }
}
