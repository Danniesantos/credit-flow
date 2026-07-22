package com.daniela.creditflow.infrastructure.persistence.installment.mapper;

import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.installment.model.Installment;
import com.daniela.creditflow.domain.installment.valueObject.InstallmentId;
import com.daniela.creditflow.domain.valueObject.Money;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.installment.entity.InstallmentEntity;
import org.springframework.stereotype.Component;

@Component
public class InstallmentMapper {

    public InstallmentEntity toEntity(Installment installment,
                                      CreditEntity credit) {

        return new InstallmentEntity(
                installment.getId().value(),
                installment.getNumber(),
                installment.getAmount().value(),
                installment.getDueDate(),
                installment.getPaymentMethod(),
                installment.getStatus(),
                credit
        );

    }

    public Installment toDomain(InstallmentEntity entity) {
        return new Installment(
                new InstallmentId(entity.getId()),
                entity.getNumber(),
                new Money(entity.getAmount()),
                entity.getDueDate(),
                entity.getStatus(),
                new CreditId(entity.getCredit().getId())
        );
    }
}
