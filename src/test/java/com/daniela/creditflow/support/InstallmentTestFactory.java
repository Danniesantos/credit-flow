package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.model.PaymentMethod;
import com.daniela.creditflow.domain.valueobject.CreditId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.daniela.creditflow.support.TestConstants.TEST_DATE;

public final class InstallmentTestFactory {

    private InstallmentTestFactory() {
    }

    public static List<Installment> installments(
            CreditId creditId,
            int quantity
    ) {
        return installments(creditId, 1, quantity);
    }

    public static List<Installment> installments(
            CreditId creditId,
            int startNumber,
            int quantity
    ) {

        List<Installment> installments = new ArrayList<>(quantity);

        for (int i = 0; i < quantity; i++) {
            int installmentNumber = startNumber + i;

            installments.add(
                    new Installment(
                            installmentNumber,
                            TestConstants.INSTALLMENT_AMOUNT,
                            TEST_DATE.plusMonths(i + 1),
                            creditId
                    )
            );
        }

        return installments;
    }

    public static Installment pendingInstallment() {
        return new Installment(
                TestConstants.INSTALLMENT_NUMBER,
                TestConstants.INSTALLMENT_AMOUNT,
                LocalDate.now().plusDays(10),
                new CreditId()
        );
    }

    public static Installment paidInstallment() {

        Installment installment = pendingInstallment();

        installment.pay(
                PaymentMethod.PIX,
                TestConstants.PAID_AT
        );

        return installment;
    }

    public static List<Installment> overdueInstallments(
            CreditId creditId,
            int startNumber,
            int quantity
    ) {

        List<Installment> installments = new ArrayList<>(quantity);

        for (int i = 0; i < quantity; i++) {

            int installmentNumber = startNumber + i;

            installments.add(
                    new Installment(
                            installmentNumber,
                            TestConstants.INSTALLMENT_AMOUNT,
                            TEST_DATE.minusMonths(i + 1),
                            creditId
                    )
            );
        }

        return installments;
    }

    public static List<Installment> overdueInstallments(
            CreditId creditId,
            int quantity
    ) {
        return overdueInstallments(creditId, 1, quantity);
    }
}
