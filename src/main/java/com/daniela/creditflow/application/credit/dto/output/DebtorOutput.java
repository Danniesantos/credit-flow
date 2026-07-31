package com.daniela.creditflow.application.credit.dto.output;

import java.math.BigDecimal;
import java.util.UUID;

public record DebtorOutput(UUID creditId,
                           UUID customerId,
                           Long overdueInstallments,
                           BigDecimal overdueAmount) {
}
