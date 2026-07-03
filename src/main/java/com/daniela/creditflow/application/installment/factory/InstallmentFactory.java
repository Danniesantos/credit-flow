package com.daniela.creditflow.application.installment.factory;

import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.installment.model.Installment;
import com.daniela.creditflow.domain.valueObject.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InstallmentFactory {

    public List<Installment> createInstallments(
            CreditId creditId,
            Integer quantity,
            Money totalAmount,
            LocalDate referenceDate,
            DueDatePolicy policy) {

        List<Installment> installments = new ArrayList<>();

        BigDecimal installmentValue =
                totalAmount.value()
                        .divide(
                                BigDecimal.valueOf(quantity),
                                2,
                                RoundingMode.HALF_UP);

        for (int i = 1; i <= quantity; i++) {

            BigDecimal value = installmentValue;

            if (i == quantity) {
                value = totalAmount.value()
                        .subtract(
                                installmentValue.multiply(
                                        BigDecimal.valueOf(quantity - 1)
                                )
                        );
            }

            LocalDate dueDate =
                    policy.calculate(i, referenceDate);

            installments.add(
                    new Installment(
                            i,
                            new Money(value),
                            dueDate,
                            creditId
                    )
            );
        }

        return installments;
    }
}
