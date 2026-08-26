package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.model.*;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.domain.valueobject.Money;

import java.time.Instant;
import java.util.UUID;

public final class CreditTestFactory {

    private static final Instant NOW =
            Instant.parse("2026-08-24T15:00:00Z");

    private CreditTestFactory() {
    }

    public static Credit underAnalysisCredit() {

        return new Credit(
                new CreditId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                TestConstants.TOTAL_CREDIT_AMOUNT,
                CreditType.PERSONAL,
                TestConstants.FIVE_PERCENT,
                12,
                NOW
        );
    }

    public static Credit restoredCredit(Credit credit) {

        return Credit.restore(
                new CreditSnapshot(
                        credit.getId(),
                        credit.getCustomerId(),
                        credit.getRequestedAmount(),
                        credit.getCreditType(),
                        credit.getInterestRate(),
                        credit.getInstallmentsQuantity(),
                        credit.getStatus(),
                        credit.getCreatedAt(),
                        credit.getUpdatedAt(),
                        credit.getInstallments()
                )
        );
    }

    public static Credit approvedCredit() {

        Credit credit = underAnalysisCredit();

        credit.approve(NOW);

        return credit;
    }

    public static Credit rejectedCredit() {

        Credit credit = underAnalysisCredit();

        credit.reject(NOW);

        return credit;
    }

    public static Credit canceledCredit() {

        Credit credit = underAnalysisCredit();

        credit.cancel(NOW);

        return credit;
    }

    public static Credit contractedCredit() {

        Credit credit = approvedCredit();

        credit.contract(
                InstallmentTestFactory.installments(
                        credit.getId(),
                        credit.getInstallmentsQuantity()
                ),
                NOW
        );

        return credit;
    }

    public static Credit paidOffCredit() {

        Credit credit = contractedCredit();

        credit.getInstallments().forEach(installment ->
                credit.markInstallmentAsPaid(
                        installment.getId(),
                        PaymentMethod.PIX,
                        TestConstants.PAID_AT,
                        NOW
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
                TestConstants.PAID_AT,
                NOW
        );

        return credit;
    }

    public static Credit creditWithOverdueInstallments() {

        Credit credit = approvedCredit();

        credit.contract(
                InstallmentTestFactory.overdueInstallments(
                        credit.getId(),
                        credit.getInstallmentsQuantity()
                ),
                NOW
        );

        return credit;
    }

    public static Credit creditWithAmount(Money amount) {

        return new Credit(
                new CreditId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                amount,
                CreditType.PERSONAL,
                TestConstants.FIVE_PERCENT,
                12,
                NOW
        );
    }

    public static Credit creditWithTypeAndAmount(
            CreditType type,
            Money amount
    ) {

        return new Credit(
                new CreditId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                amount,
                type,
                TestConstants.FIVE_PERCENT,
                12,
                NOW
        );
    }

    public static Credit creditWithPaidAndOverdueInstallments() {

        Credit credit = approvedCredit();

        credit.contract(
                InstallmentTestFactory.paidAndOverdueInstallments(
                        credit.getId(),
                        credit.getInstallmentsQuantity()
                ),
                NOW
        );

        return credit;
    }
}