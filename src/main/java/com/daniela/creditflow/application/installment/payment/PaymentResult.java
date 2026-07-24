package com.daniela.creditflow.application.installment.payment;

import java.time.Instant;

public record PaymentResult(boolean success,
                            String transactionId,
                            Instant paidAt) {
}
