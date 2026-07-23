package com.daniela.creditflow.infrastructure.web.exceptionhandler;

import com.daniela.creditflow.domain.exceptions.BusinessRuleException;
import com.daniela.creditflow.domain.exceptions.ConflictException;
import com.daniela.creditflow.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PATH = "path";
    private static final String TIMESTAMP = "timestamp";

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(
            BusinessRuleException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Business Rule Violation",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflictExceptions(
            ConflictException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.CONFLICT,
                "Resource conflict",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.NOT_FOUND,
                "Resource not found",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Invalid request body",
                "Request body contains invalid data.",
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex,
                                          HttpServletRequest request) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors,
                request
        );
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ProblemDetail handleBadRequest(Exception ex,
                                          HttpServletRequest request) {

        String detail = (ex instanceof MethodArgumentTypeMismatchException)
                ? "Invalid UUID format."
                : ex.getMessage();

        String title = (ex instanceof MethodArgumentTypeMismatchException)
                ? "Invalid parameter"
                : "Invalid input";

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                title,
                detail,
                request
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method not allowed",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex,
                                       HttpServletRequest request) {

        return buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Unexpected error",
                request
        );
    }

    private ProblemDetail buildProblem(
            HttpStatus status,
            String title,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setTitle(title);
        problem.setDetail(detail);

        if (request != null) {
            problem.setProperty(PATH, request.getRequestURI());
        }

        problem.setProperty(TIMESTAMP, Instant.now());

        return problem;
    }
}
