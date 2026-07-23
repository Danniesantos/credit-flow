package com.daniela.creditflow.application.customer.dto.output;

import com.daniela.creditflow.domain.model.CustomerStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CustomerOutput(UUID id,
                             String name,
                             String cpf,
                             String email,
                             LocalDate dateOfBirth,
                             String phoneNumber,
                             BigDecimal monthlyIncome,
                             Integer creditScore,
                             CustomerStatus status,
                             Instant createdAt,
                             Instant updatedAt) {
}
