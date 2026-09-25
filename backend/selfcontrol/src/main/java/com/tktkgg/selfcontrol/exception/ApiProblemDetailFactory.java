package com.tktkgg.selfcontrol.exception;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Creates and writes the application's common RFC 9457 error response.
 */
@Component
public class ApiProblemDetailFactory {
    private static final String TYPE_PREFIX = "urn:selfcontrol:problem:";

    private final ObjectMapper objectMapper;

    public ApiProblemDetailFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ProblemDetail create(
        HttpStatusCode status,
        String code,
        String detail,
        HttpServletRequest request
    ) {
        return create(status, code, detail, request.getRequestURI());
    }

    public ProblemDetail create(
        HttpStatusCode status,
        String code,
        String detail,
        String requestUri
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        enrich(problemDetail, status, code, requestUri);
        return problemDetail;
    }

    /**
     * Adds the application's extension properties to a ProblemDetail created
     * by Spring itself, while preserving Spring's original detail message.
     */
    public void enrich(
        ProblemDetail problemDetail,
        HttpStatusCode status,
        String fallbackCode,
        String requestUri
    ) {
        problemDetail.setStatus(status.value());

        if (problemDetail.getTitle() == null) {
            problemDetail.setTitle(resolveTitle(status));
        }
        if (problemDetail.getType() == null) {
            problemDetail.setType(toProblemType(fallbackCode));
        }
        if (problemDetail.getInstance() == null) {
            problemDetail.setInstance(toRequestUri(requestUri));
        }
        if (problemDetail.getProperties() == null
            || !problemDetail.getProperties().containsKey("code")) {
            problemDetail.setProperty("code", fallbackCode);
        }
        if (problemDetail.getProperties() == null
            || !problemDetail.getProperties().containsKey("timestamp")) {
            problemDetail.setProperty("timestamp", OffsetDateTime.now(ZoneOffset.UTC).toString());
        }
    }

    public void write(HttpServletResponse response, ProblemDetail problemDetail) throws IOException {
        response.setStatus(problemDetail.getStatus());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), problemDetail);
    }

    private String resolveTitle(HttpStatusCode status) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        return httpStatus != null ? httpStatus.getReasonPhrase() : "HTTP Error";
    }

    private URI toProblemType(String code) {
        String normalizedCode = code.toLowerCase(Locale.ROOT).replace('_', '-');
        return URI.create(TYPE_PREFIX + normalizedCode);
    }

    private URI toRequestUri(String requestUri) {
        if (requestUri == null || requestUri.isBlank()) {
            return URI.create("/");
        }
        try {
            return URI.create(requestUri);
        } catch (IllegalArgumentException exception) {
            return URI.create("/");
        }
    }
}
