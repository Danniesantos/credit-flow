package com.daniela.creditflow.infrastructure.web.response;

import com.daniela.creditflow.domain.credit.model.CreditStatus;

import java.util.UUID;

public record AnalyzeCreditResponse(UUID creditId,
                                    CreditStatus status,
                                    String reason) {
}
