package com.daniela.creditflow.infrastructure.web.response;

import java.math.BigDecimal;
import java.util.List;

public record OverdueResponse(Boolean hasOverdueInstallments,
                              Long overdueInstallmentsQuantity,
                              BigDecimal overdueAmount,
                              List<OverdueInstallmentResponse> installments) {
}
