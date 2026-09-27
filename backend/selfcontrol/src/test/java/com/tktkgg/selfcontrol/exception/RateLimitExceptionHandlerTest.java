package com.tktkgg.selfcontrol.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import tools.jackson.databind.json.JsonMapper;

class RateLimitExceptionHandlerTest {
    private final GlobalExceptionHandler handler =
        new GlobalExceptionHandler(
            new ApiProblemDetailFactory(new JsonMapper())
        );

    @Test
    void rateLimitExceptionIsConvertedTo429ProblemDetail() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/login");

        ResponseEntity<Object> response =
            handler.handleRateLimitExceeded(
                new RateLimitExceededException(300),
                request
            );

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals(
            "application/problem+json",
            response.getHeaders().getContentType().toString()
        );
        assertEquals(
            "300",
            response.getHeaders().getFirst("Retry-After")
        );

        ProblemDetail problemDetail =
            (ProblemDetail) response.getBody();

        assertNotNull(problemDetail);
        assertEquals(
            "LOGIN_RATE_LIMITED",
            problemDetail.getProperties().get("code")
        );
        assertEquals(429, problemDetail.getStatus());
        assertEquals(
            "/api/auth/login",
            problemDetail.getInstance().toString()
        );
    }
}
