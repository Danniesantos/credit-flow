package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.valueObject.CreditScore;
import com.daniela.creditflow.domain.valueObject.CPF;
import com.daniela.creditflow.domain.valueObject.Email;
import com.daniela.creditflow.domain.valueObject.PhoneNumber;
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
