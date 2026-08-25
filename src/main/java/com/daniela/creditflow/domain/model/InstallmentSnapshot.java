package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.InstallmentId;
import com.daniela.creditflow.domain.valueobject.Money;

import java.time.Instant;
import java.time.LocalDate;

public record InstallmentSnapshot(InstallmentId id,
                                  Integer number,
                                  Money amount,
                                  LocalDate dueDate,
                                  PaymentMethod paymentMethod,
                                  InstallmentStatus status,
                                  CreditId creditId,
                                  Instant paidAt) {
}
