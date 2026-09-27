package com.tktkgg.selfcontrol.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import com.tktkgg.selfcontrol.entity.User;
import com.tktkgg.selfcontrol.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthLoginRateLimitIntegrationTest {
    private static final String PASSWORD = "correct-password";
    private static final String WRONG_PASSWORD = "wrong-password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void ipLimitReturns429ThroughTheLoginEndpoint() throws Exception {
        String ipAddress = uniqueIpAddress();

        for (int attempt = 0; attempt < 15; attempt++) {
            login(
                uniqueEmail(),
                WRONG_PASSWORD,
                ipAddress
            ).andExpect(status().isUnauthorized());
        }

        MvcResult result = login(
            uniqueEmail(),
            WRONG_PASSWORD,
            ipAddress
        )
            .andExpect(status().isTooManyRequests())
            .andExpect(content().contentTypeCompatibleWith(
                MediaType.valueOf("application/problem+json")
            ))
            .andExpect(jsonPath("$.code").value("LOGIN_RATE_LIMITED"))
            .andReturn();

        assertRetryAfterIsPositiveAndWithinFirstBlock(result);
    }

    @Test
    void accountLimitAggregatesFailuresFromDifferentIpAddresses() throws Exception {
        String email = uniqueEmail();
        createUser(email);

        for (int attempt = 0; attempt < 5; attempt++) {
            login(
                email,
                WRONG_PASSWORD,
                uniqueIpAddress()
            ).andExpect(status().isUnauthorized());
        }

        MvcResult result = login(
            email,
            WRONG_PASSWORD,
            uniqueIpAddress()
        )
            .andExpect(status().isTooManyRequests())
            .andExpect(jsonPath("$.code").value("LOGIN_RATE_LIMITED"))
            .andReturn();

        assertRetryAfterIsPositiveAndWithinFirstBlock(result);
    }

    @Test
    void successfulLoginResetsAccountFailures() throws Exception {
        String email = uniqueEmail();
        String ipAddress = uniqueIpAddress();
        createUser(email);

        for (int attempt = 0; attempt < 4; attempt++) {
            login(
                email,
                WRONG_PASSWORD,
                ipAddress
            ).andExpect(status().isUnauthorized());
        }

        login(
            email,
            PASSWORD,
            ipAddress
        ).andExpect(status().isOk());

        for (int attempt = 0; attempt < 5; attempt++) {
            login(
                email,
                WRONG_PASSWORD,
                ipAddress
            ).andExpect(status().isUnauthorized());
        }

        login(
            email,
            WRONG_PASSWORD,
            ipAddress
        ).andExpect(status().isTooManyRequests());
    }

    private ResultActions login(
        String email,
        String password,
        String ipAddress
    ) throws Exception {
        return mockMvc.perform(
            post("/api/auth/login")
                .with(csrf())
                .with(remoteAddress(ipAddress))
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson(email, password))
        );
    }

    private void createUser(String email) {
        User user = new User();
        user.setUsername("rate-test-user");
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(PASSWORD));
        userRepository.saveAndFlush(user);
    }

    private String loginJson(String email, String password) {
        return "{\"email\":\"" + email
            + "\",\"password\":\"" + password + "\"}";
    }

    private RequestPostProcessor remoteAddress(String ipAddress) {
        return request -> {
            request.setRemoteAddr(ipAddress);
            return request;
        };
    }

    private void assertRetryAfterIsPositiveAndWithinFirstBlock(
        MvcResult result
    ) {
        String retryAfter = result.getResponse().getHeader("Retry-After");
        assertNotNull(retryAfter);

        long retryAfterSeconds = Long.parseLong(retryAfter);
        assertTrue(retryAfterSeconds > 0);
        assertTrue(retryAfterSeconds <= 5 * 60);
    }

    private String uniqueEmail() {
        return "rate-test-" + UUID.randomUUID() + "@example.com";
    }

    private String uniqueIpAddress() {
        return "test-ip-" + UUID.randomUUID();
    }
}
