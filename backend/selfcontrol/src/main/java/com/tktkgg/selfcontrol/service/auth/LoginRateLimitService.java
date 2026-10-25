package com.tktkgg.selfcontrol.service.auth;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.exception.RateLimitExceededException;

@Service 
public class LoginRateLimitService {
    private static final String IP_KEY_PREFIX = "self-control:login-rate-limit-ip:";
    private static final String ACCOUNT_KEY_PREFIX = "self-control:login-rate-limit:account:";

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

    private final RedisLoginRateLimitStore redisStore;

    public LoginRateLimitService(Clock clock, RedisLoginRateLimitStore redisStore) {
        this.clock = clock;
        this.redisStore = redisStore;
    }

    public void checkIp(String ipAddress) {
        String redisKey = IP_KEY_PREFIX + ipAddress;

        Optional<Instant> blockedUntil = redisStore.consumeIp(
            redisKey,
            IP_POLICY,
            clock.instant()
        );
        
        rejectIfBlocked(blockedUntil);
    }

    public void checkAccount(String accountKey) {
        String redisKey = ACCOUNT_KEY_PREFIX + accountKey;

        Optional<Instant> blockedUntil = redisStore.checkAccount(
            redisKey, 
            ACCOUNT_POLICY,
            clock.instant()
        );
        
        rejectIfBlocked(blockedUntil);
    }

    public void recordAccountFailure(String accountKey) {
        String redisKey = ACCOUNT_KEY_PREFIX + accountKey;

        redisStore.recordAccountFailure(
            redisKey,
            ACCOUNT_POLICY,
            clock.instant()
        );
    }

    public void resetAccount(String accountKey) {
        String redisKey = ACCOUNT_KEY_PREFIX + accountKey;
        redisStore.resetAccount(redisKey);
    }

    private void rejectIfBlocked(Optional<Instant> blockedUntil) {
        if (blockedUntil.isEmpty()) {
            return;
        }

        long retryAfterSeconds = Math.max(
            1, 
            Duration.between(clock.instant(), blockedUntil.get()).toSeconds()
        );

        throw new RateLimitExceededException(retryAfterSeconds);
    }
}
