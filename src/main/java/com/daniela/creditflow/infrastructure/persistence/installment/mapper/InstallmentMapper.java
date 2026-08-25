package com.daniela.creditflow.infrastructure.persistence.installment.mapper;

import com.daniela.creditflow.domain.model.InstallmentSnapshot;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.valueobject.InstallmentId;
import com.daniela.creditflow.domain.valueobject.Money;
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
                installment.getPaidAt(),
                credit
        );

    }

    public Installment toDomain(InstallmentEntity entity) {

        return Installment.restore(
                new InstallmentSnapshot(
                        new InstallmentId(entity.getId()),
                        entity.getNumber(),
                        new Money(entity.getAmount()),
                        entity.getDueDate(),
                        entity.getPaymentMethod(),
                        entity.getStatus(),
                        new CreditId(entity.getCredit().getId()),
                        entity.getPaidAt()
                )
        );
    }
}
