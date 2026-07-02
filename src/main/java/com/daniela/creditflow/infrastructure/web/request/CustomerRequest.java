package com.daniela.creditflow.infrastructure.web.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CustomerRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must have at least 3 characters")
    private String name;

    @NotBlank(message = "Cpf is required")
    @CPF(message = "Invalid CPF")
    private String cpf;

    @NotBlank(message = "Email is required")
    @Email(message = "Email address is invalid")
    private String email;

    @NotNull(message = "dateOfBirth is required")
    @Past(message = "Date must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "phoneNumber is required")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "Phone number must contain 10 or 11 digits"
    )
    private String phoneNumber;

    @NotNull(message = "Monthly income is required")
    @Positive(message = "Monthly income must be greater than zero")
    private BigDecimal monthlyIncome;

    @NotNull(message = "Credit score is required")
    @Min(value = 0, message = "Credit score must be at least 0")
    @Max(value = 1000, message = "Credit score must be at most 1000")
    private Integer creditScore;

    public CustomerRequest(String name,
                           String cpf,
                           String email,
                           LocalDate dateOfBirth,
                           String phoneNumber,
                           BigDecimal monthlyIncome,
                           Integer creditScore) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.monthlyIncome = monthlyIncome;
        this.creditScore = creditScore;
    }
}
