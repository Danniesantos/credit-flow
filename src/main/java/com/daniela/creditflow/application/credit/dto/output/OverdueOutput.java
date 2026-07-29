package com.daniela.creditflow.application.credit.dto.output;

import com.daniela.creditflow.application.installment.dto.output.OverdueInstallmentOutput;

import java.math.BigDecimal;
import java.util.List;

public record OverdueOutput(Boolean hasOverdueInstallments,
                            Long overdueInstallmentsQuantity,
                            BigDecimal overdueAmount,
                            List<OverdueInstallmentOutput> installments) {

}
