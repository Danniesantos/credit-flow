package com.daniela.creditflow.application.customer.dto.input;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCustomerInput(String name,
                                  String cpf,
                                  String email,
                                  LocalDate dateOfBirth,
                                  String phoneNumber,
                                  BigDecimal monthlyIncome,
                                  Integer creditScore) {
}
