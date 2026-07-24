package com.daniela.creditflow.application.credit.dto.output;

import java.math.BigDecimal;

public record BalanceOutput(BigDecimal totalContractAmount,
                            BigDecimal paidAmount,
                            BigDecimal remainingAmount,
                            Integer remainingInstallments) {
}
