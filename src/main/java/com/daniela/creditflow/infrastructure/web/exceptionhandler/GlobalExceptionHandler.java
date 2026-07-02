package com.daniela.creditflow.infrastructure.web.exceptionhandler;

import com.daniela.creditflow.domain.customer.exception.*;
import com.daniela.creditflow.domain.exceptions.CreditNotFoundException;
import com.daniela.creditflow.domain.exceptions.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
    
    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(
            DomainException ex,
            HttpServletRequest request) {

        return buildProblem(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Business Rule Violation",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler({
            CpfAlreadyExistsException.class,
            EmailAlreadyExistsException.class,
            CustomerAlreadyInactiveException.class,
            CustomerHasOpenCreditsException.class,
            DataIntegrityViolationException.class
    })
    public ProblemDetail handleConflictExceptions(
            Exception ex,
            HttpServletRequest request) {

        String title = switch (ex.getClass().getSimpleName()) {
            case "CpfAlreadyExistsException" -> "CPF already registered";
            case "EmailAlreadyExistsException" -> "Email already registered";
            case "CustomerAlreadyInactiveException" -> "Customer already inactive";
            case "CustomerHasOpenCreditsException" -> "Customer has open credits";
            default -> "Data integrity violation";
        };

        String detail = (ex instanceof DataIntegrityViolationException)
                ? "A unique field already exists."
                : ex.getMessage();

        return buildProblem(
                HttpStatus.CONFLICT,
                title,
                detail,
                request
        );
    }

    @ExceptionHandler({
            CustomerNotFoundException.class,
            CreditNotFoundException.class
    })
    public ProblemDetail handleNotFound(
            RuntimeException ex,
            HttpServletRequest request) {

        String title = switch (ex.getClass().getSimpleName()) {
            case "CustomerNotFoundException" -> "Customer Not Found";
            default -> "Credit Not Found";
        };

        return buildProblem(
                HttpStatus.NOT_FOUND,
                title,
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildProblem(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors,
                null
        );
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ProblemDetail handleBadRequest(Exception ex, HttpServletRequest request) {

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
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {

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
