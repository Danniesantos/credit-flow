package com.daniela.creditflow.infrastructure.web.exceptionhandler;

import com.daniela.creditflow.domain.exceptions.*;
import com.daniela.creditflow.infrastructure.web.request.CustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.setRequestURI("/credits/123");

        MockHttpServletRequest httpRequest =
                new MockHttpServletRequest();

        httpRequest.setRequestURI("/credits/123");
    }

    @Test
    @DisplayName("Should handle business rule exception")
    void shouldHandleBusinessRuleException() {

        BusinessRuleException exception =
                new CustomerHasOpenCreditsException();

        ProblemDetail result =
                handler.handleBusinessRule(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(422);

        assertThat(result.getTitle())
                .isEqualTo("Business Rule Violation");

        assertThat(result.getDetail())
                .isEqualTo(exception.getMessage());

        assertThat(result.getProperties())
                .containsEntry("path", "/credits/123")
                .containsKey("timestamp");
    }

    @Test
    @DisplayName("Should handle conflict exception")
    void shouldHandleConflictException() {

        ConflictException exception =
                new CpfAlreadyExistsException();

        ProblemDetail result =
                handler.handleConflictExceptions(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(409);

        assertThat(result.getTitle())
                .isEqualTo("Resource conflict");

        assertThat(result.getDetail())
                .isEqualTo(exception.getMessage());

        assertThat(result.getProperties())
                .containsEntry("path", "/credits/123")
                .containsKey("timestamp");
    }

    @Test
    @DisplayName("Should handle resource not found exception")
    void shouldHandleResourceNotFoundException() {

        ResourceNotFoundException exception =
                new CustomerNotFoundException();

        ProblemDetail result =
                handler.handleNotFound(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(404);

        assertThat(result.getTitle())
                .isEqualTo("Resource not found");

        assertThat(result.getDetail())
                .isEqualTo(exception.getMessage());

        assertThat(result.getProperties())
                .containsEntry("path", "/credits/123")
                .containsKey("timestamp");
    }

    @Test
    @DisplayName("Should handle invalid request body")
    void shouldHandleInvalidRequestBody() {

        HttpMessageNotReadableException exception =
                new HttpMessageNotReadableException(
                        "Invalid body",
                        new MockHttpInputMessage(new byte[0])
                );

        ProblemDetail result =
                handler.handleHttpMessageNotReadable(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(400);

        assertThat(result.getTitle())
                .isEqualTo("Invalid request body");

        assertThat(result.getDetail())
                .isEqualTo("Request body contains invalid data.");

        assertThat(result.getProperties())
                .containsEntry("path", "/credits/123")
                .containsKey("timestamp");
    }

    @Test
    @DisplayName("Should handle validation errors")
    void shouldHandleValidationErrors() {

        CustomerRequest customerRequest =
                new CustomerRequest(
                        "",
                        "invalid-cpf",
                        "invalid-email",
                        null,
                        "123",
                        BigDecimal.ZERO,
                        1001
                );

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(
                        customerRequest,
                        "customerRequest"
                );

        bindingResult.addError(
                new FieldError(
                        "customerRequest",
                        "name",
                        "Name is required"
                )
        );

        bindingResult.addError(
                new FieldError(
                        "customerRequest",
                        "email",
                        "Email address is invalid"
                )
        );

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(
                        null,
                        bindingResult
                );

        MockHttpServletRequest httpRequest =
                new MockHttpServletRequest();

        httpRequest.setRequestURI("/credits/123");

        ProblemDetail result =
                handler.handleValidation(
                        exception,
                        httpRequest
                );

        assertThat(result.getStatus())
                .isEqualTo(400);

        assertThat(result.getTitle())
                .isEqualTo("Validation failed");

        assertThat(result.getDetail())
                .isEqualTo(
                        "name: Name is required, email: Email address is invalid"
                );

        assertThat(result.getProperties())
                .containsEntry("path", "/credits/123")
                .containsKey("timestamp");
    }

    @Test
    @DisplayName("Should handle illegal argument exception")
    void shouldHandleIllegalArgumentException() {

        IllegalArgumentException exception =
                new IllegalArgumentException("Invalid input");

        ProblemDetail result =
                handler.handleBadRequest(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(400);

        assertThat(result.getTitle())
                .isEqualTo("Invalid input");

        assertThat(result.getDetail())
                .isEqualTo("Invalid input");
    }

    @Test
    @DisplayName("Should handle invalid UUID parameter")
    void shouldHandleInvalidUuidParameter() {

        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException(
                        "id",
                        UUID.class,
                        "invalid",
                        null,
                        new IllegalArgumentException()
                );

        ProblemDetail result =
                handler.handleBadRequest(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(400);

        assertThat(result.getTitle())
                .isEqualTo("Invalid parameter");

        assertThat(result.getDetail())
                .isEqualTo("Invalid UUID format.");
    }

    @Test
    @DisplayName("Should handle method not allowed")
    void shouldHandleMethodNotAllowed() {

        HttpRequestMethodNotSupportedException exception =
                new HttpRequestMethodNotSupportedException(
                        "GET"
                );

        ProblemDetail result =
                handler.handleMethodNotAllowed(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(405);

        assertThat(result.getTitle())
                .isEqualTo("Method not allowed");

        assertThat(result.getProperties())
                .containsEntry("path", "/credits/123")
                .containsKey("timestamp");
    }

    @Test
    @DisplayName("Should handle unexpected exception")
    void shouldHandleUnexpectedException() {

        Exception exception =
                new RuntimeException("Database exploded");

        ProblemDetail result =
                handler.handleGeneric(
                        exception,
                        request
                );

        assertThat(result.getStatus())
                .isEqualTo(500);

        assertThat(result.getTitle())
                .isEqualTo("Internal Server Error");

        assertThat(result.getDetail())
                .isEqualTo("Unexpected error");

        assertThat(result.getProperties())
                .containsEntry("path", "/credits/123")
                .containsKey("timestamp");
    }
}
