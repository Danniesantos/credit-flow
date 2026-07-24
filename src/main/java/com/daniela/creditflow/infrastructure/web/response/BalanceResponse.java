package com.daniela.creditflow.infrastructure.web.response;

import java.math.BigDecimal;

public record BalanceResponse(BigDecimal totalContractAmount,
                              BigDecimal paidAmount,
                              BigDecimal remainingAmount,
                              Integer remainingInstallments) {
}
