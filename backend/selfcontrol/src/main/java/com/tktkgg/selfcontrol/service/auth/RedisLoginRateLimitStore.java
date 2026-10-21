package com.tktkgg.selfcontrol.service.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

@Component 
public class RedisLoginRateLimitStore {
    private final StringRedisTemplate redisTemplate;

    @SuppressWarnings("rawtypes")
    private final RedisScript<List> rateLimitScript;

    @SuppressWarnings("rawtypes")
    public RedisLoginRateLimitStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        DefaultRedisScript<List> script = new DefaultRedisScript<>();
        script.setLocation(
            new ClassPathResource("redis/login-rate-limit.lua")
        );
        script.setResultType(List.class);

        this.rateLimitScript = script;
    }

    public Optional<Instant> consumeIp(
        String redisKey,
        RateLimitPolicy policy,
        Instant now
    ) {
        return executePolicy(
            "IP_CONSUME",
            redisKey,
            policy,
            now
        );
    }

    public Optional<Instant> checkAccount(
        String redisKey,
        RateLimitPolicy policy,
        Instant now
    ) {
        return executePolicy(
            "ACCOUNT_CHECK",
            redisKey,
            policy,
            now
        );
    }

    public void recordAccountFailure(
        String redisKey,
        RateLimitPolicy policy,
        Instant now
    ) {
        executePolicy(
            "ACCOUNT_FAILURE",
            redisKey,
            policy,
            now
        );
    }

    public void resetAccount(String redisKey) {
        List<?> response = executeScript(
            redisKey,
            "RESET_ACCOUNT"
        );

        if (
            response == null
            || response.size() != 2
            || !"RESET".equals(String.valueOf(response.get(0)))
        ) {
            throw new IllegalStateException(
                "Unexpected response from Redis rate limit script."
            );
        }
    }

    private Optional<Instant> executePolicy(
        String operation,
        String redisKey,
        RateLimitPolicy policy,
        Instant now
    ) {
        List<String> arguments = new ArrayList<>();

        arguments.add(operation);
        arguments.add(Long.toString(now.toEpochMilli()));
        arguments.add(Integer.toString(policy.threshold()));
        arguments.add(Long.toString(policy.window().toMillis()));
        arguments.add(Long.toString(policy.quietPeriod().toMillis()));
        arguments.add(Integer.toString(policy.blockDurations().size()));

        for (Duration blockDuration : policy.blockDurations()) {
            arguments.add(Long.toString(blockDuration.toMillis()));
        }

        List<?> response = executeScript(
            redisKey,
            arguments.toArray(String[]::new)
        );

        return readBlockedUntil(response);
    }

    private List<?> executeScript(String redisKey, String... arguments) {
        List<?> response = redisTemplate.execute(
            rateLimitScript,
            List.of(redisKey),
            (Object[]) arguments
        );

        if (response == null || response.size() != 2) {
            throw new IllegalStateException(
                "Unexpected response from Redis rate limit script."
            );
        }

        return response;
    }

    private Optional<Instant> readBlockedUntil(List<?> response) {
        String status = String.valueOf(response.get(0));

        if ("ALLOW".equals(status)) {
            return Optional.empty();
        }

        if ("BLOCKED".equals(status)) {
            try {
                long blockedUntilEpochMills = Long.parseLong(String.valueOf(response.get(1)));

                return Optional.of(Instant.ofEpochMilli(blockedUntilEpochMills));
            } catch (NumberFormatException exception) {
                throw new IllegalStateException(
                    "Redis returned as invalid blocked-until timestamp",
                    exception
                );
            }
        }

        throw new IllegalStateException(
            "Unknown status from Redis rate limit script:" + status
        );
    }
}
