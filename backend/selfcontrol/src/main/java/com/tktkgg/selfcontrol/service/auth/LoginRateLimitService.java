package com.tktkgg.selfcontrol.service.auth;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.tktkgg.selfcontrol.service.auth.RateLimitPolicy;

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
}
