package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.valueobject.CreditScore;
import com.daniela.creditflow.domain.valueobject.CPF;
import com.daniela.creditflow.domain.valueobject.Email;
import com.daniela.creditflow.domain.valueobject.PhoneNumber;
import com.daniela.creditflow.domain.valueobject.*;

import java.time.LocalDate;

public record CustomerData(String name,
                           CPF cpf,
                           Email email,
                           LocalDate dateOfBirth,
                           PhoneNumber phoneNumber,
                           CreditScore creditScore,
                           Money monthlyIncome) {
}
