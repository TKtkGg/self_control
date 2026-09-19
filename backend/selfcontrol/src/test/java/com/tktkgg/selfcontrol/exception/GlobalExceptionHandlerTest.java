package com.tktkgg.selfcontrol.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import tools.jackson.databind.json.JsonMapper;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler(
        new ApiProblemDetailFactory(new JsonMapper())
    );

    @Test
    void expectedApiExceptionIsConvertedToProblemDetail() {
        ResponseEntity<Object> response = handler.handleApiException(
            ApiException.conflict("EMAIL_ALREADY_IN_USE", "The email address is already in use."),
            request("/api/auth/signup")
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("application/problem+json", response.getHeaders().getContentType().toString());

        ProblemDetail problemDetail = (ProblemDetail) response.getBody();
        assertNotNull(problemDetail);
        assertEquals("EMAIL_ALREADY_IN_USE", problemDetail.getProperties().get("code"));
        assertEquals("The email address is already in use.", problemDetail.getDetail());
        assertEquals("/api/auth/signup", problemDetail.getInstance().toString());
    }

    @Test
    void internalErrorDoesNotExposeInternalDetail() {
        ResponseEntity<Object> response = handler.handleUnexpectedException(
            new IllegalStateException("database password must not be exposed"),
            request("/api/schedules")
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ProblemDetail problemDetail = (ProblemDetail) response.getBody();
        assertNotNull(problemDetail);
        assertEquals("INTERNAL_SERVER_ERROR", problemDetail.getProperties().get("code"));
        assertEquals("An unexpected server error occurred.", problemDetail.getDetail());
        org.junit.jupiter.api.Assertions.assertFalse(
            problemDetail.getDetail().contains("database password")
        );
    }

    private MockHttpServletRequest request(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI(uri);
        return request;
    }
}
