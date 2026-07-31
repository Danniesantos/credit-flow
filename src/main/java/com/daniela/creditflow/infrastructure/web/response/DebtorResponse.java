package com.daniela.creditflow.infrastructure.web.response;

import java.math.BigDecimal;
import java.util.UUID;

public record DebtorResponse(UUID creditId,
                             UUID customerId,
                             Long overdueInstallments,
                             BigDecimal overdueAmount) {
}
