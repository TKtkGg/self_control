package com.tktkgg.selfcontrol.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import tools.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;

import java.time.DateTimeException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converts application and MVC exceptions into one ProblemDetail format.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";
    private static final String INTERNAL_ERROR_CODE = "INTERNAL_SERVER_ERROR";
    private static final String INTERNAL_ERROR_DETAIL = "An unexpected server error occurred.";

    private final ApiProblemDetailFactory problemDetailFactory;

    public GlobalExceptionHandler(ApiProblemDetailFactory problemDetailFactory) {
        this.problemDetailFactory = problemDetailFactory;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException exception, HttpServletRequest request) {
        if (exception.getStatus().is5xxServerError()) {
            logger.error(
                "API error at " + request.getMethod() + " " + request.getRequestURI(),
                exception
            );
        } else {
            logger.warn(
                "API error at " + request.getMethod() + " " + request.getRequestURI()
                    + ": " + exception.getMessage()
            );
        }

        String detail = exception.getStatus().is5xxServerError()
            ? INTERNAL_ERROR_DETAIL
            : exception.getMessage();
        ProblemDetail problemDetail = problemDetailFactory.create(
            exception.getStatus(),
            exception.getCode(),
            detail,
            request
        );
        return toResponse(problemDetail, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(
        DataIntegrityViolationException exception,
        HttpServletRequest request
    ) {
        logger.warn(
            "Data integrity violation at " + request.getMethod() + " " + request.getRequestURI(),
            exception
        );
        ProblemDetail problemDetail = problemDetailFactory.create(
            HttpStatus.CONFLICT,
            "DATA_CONFLICT",
            "The request conflicts with existing data.",
            request
        );
        return toResponse(problemDetail, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
        ConstraintViolationException exception,
        HttpServletRequest request
    ) {
        ProblemDetail problemDetail = problemDetailFactory.create(
            HttpStatus.BAD_REQUEST,
            VALIDATION_ERROR_CODE,
            "Request validation failed.",
            request
        );
        problemDetail.setProperty("errors", exception.getConstraintViolations().stream()
            .map(violation -> Map.of(
                "field", violation.getPropertyPath().toString(),
                "message", violation.getMessage()
            ))
            .toList());
        return toResponse(problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
        IllegalArgumentException exception,
        HttpServletRequest request
    ) {
        logger.warn(
            "Invalid argument at " + request.getMethod() + " " + request.getRequestURI()
                + ": " + exception.getMessage()
        );
        ProblemDetail problemDetail = problemDetailFactory.create(
            HttpStatus.BAD_REQUEST,
            "INVALID_REQUEST",
            "The request is invalid.",
            request
        );
        return toResponse(problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Object> handleDateTimeException(
        DateTimeException exception,
        HttpServletRequest request
    ) {
        logger.warn(
            "Invalid date or time at " + request.getMethod() + " " + request.getRequestURI()
                + ": " + exception.getMessage()
        );
        ProblemDetail problemDetail = problemDetailFactory.create(
            HttpStatus.BAD_REQUEST,
            "INVALID_DATE_OR_TIME",
            "A date or time value is invalid.",
            request
        );
        return toResponse(problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        logger.error(
            "Unhandled exception at " + request.getMethod() + " " + request.getRequestURI(),
            exception
        );
        ProblemDetail problemDetail = problemDetailFactory.create(
            HttpStatus.INTERNAL_SERVER_ERROR,
            INTERNAL_ERROR_CODE,
            INTERNAL_ERROR_DETAIL,
            request
        );
        return toResponse(problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            VALIDATION_ERROR_CODE,
            "Request validation failed.",
            request
        );
        problemDetail.setProperty("errors", toValidationErrors(exception.getBindingResult()));
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
        HandlerMethodValidationException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            VALIDATION_ERROR_CODE,
            "Request validation failed.",
            request
        );
        problemDetail.setProperty("errors", toValidationErrors(exception));
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        String detail = exception.getCause() instanceof InvalidFormatException
            ? "A request field has an invalid format."
            : "The request body is missing or malformed.";
        ProblemDetail problemDetail = problemDetail(status, "MALFORMED_REQUEST", detail, request);
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        TypeMismatchException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            "INVALID_PARAMETER",
            "A request parameter has an invalid format.",
            request
        );
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            "MISSING_PARAMETER",
            "Required request parameter is missing: " + exception.getParameterName() + ".",
            request
        );
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
        MissingPathVariableException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            "MISSING_PATH_VARIABLE",
            "Required path variable is missing: " + exception.getVariableName() + ".",
            request
        );
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
        ServletRequestBindingException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            "INVALID_REQUEST_BINDING",
            "A request value could not be bound.",
            request
        );
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            "METHOD_NOT_ALLOWED",
            "The HTTP method is not supported for this endpoint.",
            request
        );
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            "UNSUPPORTED_MEDIA_TYPE",
            "The request media type is not supported.",
            request
        );
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
        HttpMediaTypeNotAcceptableException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail = problemDetail(
            status,
            "NOT_ACCEPTABLE",
            "The requested response format is not supported.",
            request
        );
        return toResponse(problemDetail, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception exception,
        Object body,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        if (status.is5xxServerError()) {
            logger.error("MVC exception at " + request.getDescription(false), exception);
        }
        return super.handleExceptionInternal(exception, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(
        Object body,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        ProblemDetail problemDetail;
        if (status.is5xxServerError()) {
            problemDetail = problemDetail(
                status,
                INTERNAL_ERROR_CODE,
                INTERNAL_ERROR_DETAIL,
                request
            );
        } else if (body instanceof ProblemDetail existingProblemDetail) {
            problemDetail = existingProblemDetail;
            problemDetailFactory.enrich(
                problemDetail,
                status,
                defaultCode(status),
                requestUri(request)
            );
        } else {
            problemDetail = problemDetail(
                status,
                defaultCode(status),
                defaultDetail(status),
                request
            );
        }
        return toResponse(problemDetail, headers, status);
    }

    private ProblemDetail problemDetail(
        HttpStatusCode status,
        String code,
        String detail,
        WebRequest request
    ) {
        return problemDetailFactory.create(status, code, detail, requestUri(request));
    }

    private ResponseEntity<Object> toResponse(
        ProblemDetail problemDetail,
        HttpHeaders headers,
        HttpStatusCode status
    ) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.putAll(headers);
        responseHeaders.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<>(problemDetail, responseHeaders, status);
    }

    private String requestUri(WebRequest request) {
        String description = request.getDescription(false);
        return description.startsWith("uri=") ? description.substring(4) : description;
    }

    private List<Map<String, String>> toValidationErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
            .map(error -> {
                String field = error instanceof FieldError fieldError
                    ? fieldError.getField()
                    : error.getObjectName();
                return Map.of("field", field, "message", message(error));
            })
            .toList();
    }

    private List<Map<String, String>> toValidationErrors(HandlerMethodValidationException exception) {
        List<Map<String, String>> errors = new ArrayList<>();

        for (ParameterValidationResult result : exception.getParameterValidationResults()) {
            if (result instanceof ParameterErrors parameterErrors) {
                for (FieldError fieldError : parameterErrors.getFieldErrors()) {
                    errors.add(Map.of(
                        "field", fieldError.getField(),
                        "message", message(fieldError)
                    ));
                }
                for (ObjectError objectError : parameterErrors.getGlobalErrors()) {
                    errors.add(Map.of(
                        "field", objectError.getObjectName(),
                        "message", message(objectError)
                    ));
                }
            } else {
                String parameterName = result.getMethodParameter().getParameterName();
                if (parameterName == null) {
                    parameterName = "parameter[" + result.getMethodParameter().getParameterIndex() + "]";
                }
                for (var error : result.getResolvableErrors()) {
                    errors.add(Map.of(
                        "field", parameterName,
                        "message", message(error)
                    ));
                }
            }
        }

        for (var error : exception.getCrossParameterValidationResults()) {
            errors.add(Map.of("field", "request", "message", message(error)));
        }

        return errors;
    }

    private String message(org.springframework.context.MessageSourceResolvable error) {
        String defaultMessage = error.getDefaultMessage();
        return defaultMessage != null ? defaultMessage : "The value is invalid.";
    }

    private String defaultCode(HttpStatusCode status) {
        return switch (status.value()) {
            case 400 -> "INVALID_REQUEST";
            case 401 -> "UNAUTHORIZED";
            case 403 -> "FORBIDDEN";
            case 404 -> "RESOURCE_NOT_FOUND";
            case 405 -> "METHOD_NOT_ALLOWED";
            case 406 -> "NOT_ACCEPTABLE";
            case 409 -> "DATA_CONFLICT";
            case 415 -> "UNSUPPORTED_MEDIA_TYPE";
            default -> "HTTP_ERROR";
        };
    }

    private String defaultDetail(HttpStatusCode status) {
        return switch (status.value()) {
            case 400 -> "The request is invalid.";
            case 401 -> "Authentication is required.";
            case 403 -> "You do not have permission to access this resource.";
            case 404 -> "The requested resource was not found.";
            case 405 -> "The HTTP method is not supported for this endpoint.";
            case 406 -> "The requested response format is not supported.";
            case 415 -> "The request media type is not supported.";
            default -> "The request could not be completed.";
        };
    }
}
