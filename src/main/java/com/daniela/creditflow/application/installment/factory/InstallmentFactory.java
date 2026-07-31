package com.daniela.creditflow.application.installment.factory;

import com.daniela.creditflow.application.installment.policy.DueDatePolicy;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.valueObject.CreditId;
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
            Integer startNumber,
            Integer quantity,
            Money totalAmount,
            LocalDate referenceDate,
            DueDatePolicy policy) {

        validateQuantity(quantity);

        List<Installment> installments = new ArrayList<>();

        BigDecimal installmentValue =
                totalAmount.value()
                        .divide(
                                BigDecimal.valueOf(quantity),
                                2,
                                RoundingMode.HALF_UP);

        for (int i = 0; i < quantity; i++) {

            int installmentNumber = startNumber + i;

            BigDecimal value = installmentValue;

            if (i == quantity - 1) {
                value = totalAmount.value()
                        .subtract(
                                installmentValue.multiply(
                                        BigDecimal.valueOf(quantity - 1)
                                )
                        );
            }

            LocalDate dueDate =
                    policy.calculate(
                            i + 1,
                            referenceDate
                    );

            installments.add(
                    new Installment(
                            installmentNumber,
                            new Money(value),
                            dueDate,
                            creditId
                    )
            );
        }

        return installments;
    }

    private void validateQuantity(Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new InvalidDomainStateException(
                    "Installment quantity must be greater than zero"
            );
        }
    }
}
