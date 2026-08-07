package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.valueObject.CreditId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InstallmentTestFactory {

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

        List<Installment> installments = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            int installmentNumber = startNumber + i;

            installments.add(
                    new Installment(
                            installmentNumber,
                            TestConstants.ONE_THOUSAND,
                            LocalDate.now().plusMonths(i + 1),
                            creditId
                    )
            );
        }

        return installments;
    }

    public static List<Installment> overdueInstallments(
            CreditId creditId,
            int startNumber,
            int quantity
    ) {

        List<Installment> installments = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {

            int installmentNumber = startNumber + i;

            installments.add(
                    new Installment(
                            installmentNumber,
                            TestConstants.ONE_THOUSAND,
                            LocalDate.now().minusMonths(i + 1),
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
