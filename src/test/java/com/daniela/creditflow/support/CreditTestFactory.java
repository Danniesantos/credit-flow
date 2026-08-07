package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.model.*;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.valueObject.CustomerId;

import java.time.Instant;
import java.util.UUID;

public final class CreditTestFactory {

    private CreditTestFactory() {
    }

    public static Credit underAnalysisCredit() {

        return new Credit(
                new CreditId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                TestConstants.TEN_THOUSAND,
                CreditType.PERSONAL,
                TestConstants.FIVE_PERCENT,
                12,
                CreditStatus.UNDER_ANALYSIS,
                Instant.now(),
                Instant.now()
        );
    }

    public static Credit restoredCredit(Credit credit) {
        return Credit.restore(
                credit.getId(),
                credit.getCustomerId(),
                credit.getRequestedAmount(),
                credit.getCreditType(),
                credit.getInterestRate(),
                credit.getInstallmentsQuantity(),
                credit.getStatus(),
                credit.getInstallments(),
                credit.getCreatedAt(),
                credit.getUpdatedAt()
        );
    }

    public static Credit approvedCredit() {

        Credit credit = underAnalysisCredit();
        credit.approve();
        return credit;
    }

    public static Credit rejectedCredit() {
        Credit credit = underAnalysisCredit();
        credit.reject();
        return credit;
    }

    public static Credit canceledCredit() {
        Credit credit = underAnalysisCredit();
        credit.cancel();
        return credit;
    }

    public static Credit contractedCredit() {
        Credit credit = approvedCredit();

        credit.contract(
                InstallmentTestFactory.installments(
                        credit.getId(),
                        credit.getInstallmentsQuantity()
                )
        );

        return credit;
    }

    public static Credit paidOffCredit() {

        Credit credit = contractedCredit();

        credit.getInstallments().forEach(installment ->
                credit.markInstallmentAsPaid(
                        installment.getId(),
                        PaymentMethod.PIX,
                        Instant.now()
                )
        );

        return credit;
    }

    public static Credit creditWithOnePaidInstallment() {

        Credit credit = contractedCredit();

        Installment installment =
                credit.getInstallments().getFirst();

        credit.markInstallmentAsPaid(
                installment.getId(),
                PaymentMethod.PIX,
                Instant.now()
        );

        return credit;
    }

    public static Credit creditWithOverdueInstallments() {

        Credit credit = approvedCredit();

        credit.contract(
                InstallmentTestFactory.overdueInstallments(
                        credit.getId(),
                        credit.getInstallmentsQuantity()
                )
        );

        return credit;
    }

}