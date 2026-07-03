package com.daniela.creditflow.domain.credit.model;

import java.util.EnumSet;
import java.util.Set;

public enum CreditStatus {
    UNDER_ANALYSIS,
    APPROVED,
    REJECTED,
    CONTRACTED,
    PAID_OFF;

    public static Set<CreditStatus> openStatuses() {
        return EnumSet.of(
                UNDER_ANALYSIS,
                APPROVED,
                CONTRACTED
        );
    }
}
