package com.daniela.creditflow.domain.customer.model;

import com.daniela.creditflow.domain.customer.valueObject.CPF;
import com.daniela.creditflow.domain.customer.valueObject.Email;
import com.daniela.creditflow.domain.customer.valueObject.PhoneNumber;
import com.daniela.creditflow.domain.valueObject.*;

import java.time.LocalDate;

public record CustomerData(String name,
                           CPF cpf,
                           Email email,
                           LocalDate dateOfBirth,
                           PhoneNumber phoneNumber,
                           CreditScore creditScore,
                           Money monthlyIncome) {
}
