package com.daniela.creditflow.application.credit.dto.output;

import com.daniela.creditflow.domain.model.CreditStatus;

import java.util.UUID;

public record AnalyzeCreditOutput(UUID creditId,
                                  CreditStatus status,
                                  String reason) {
}
