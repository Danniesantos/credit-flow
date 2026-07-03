package com.daniela.creditflow.infrastructure.persistence.credity.mapper;

import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.domain.installment.model.Installment;
import com.daniela.creditflow.domain.valueObject.InterestRate;
import com.daniela.creditflow.domain.valueObject.Money;
import com.daniela.creditflow.infrastructure.persistence.credity.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import com.daniela.creditflow.infrastructure.persistence.installment.mapper.InstallmentMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreditMapper {

    private final InstallmentMapper installmentMapper;

    public CreditMapper(InstallmentMapper installmentMapper) {
        this.installmentMapper = installmentMapper;
    }

    public CreditEntity toEntity(Credit credit) {
        CustomerEntity customer =
                new CustomerEntity(
                        credit.getCustomerId().value());

        CreditEntity creditEntity =
                new CreditEntity(
                        credit.getId().value(),
                        customer,
                        credit.getRequestedAmount().value(),
                        credit.getCreditType(),
                        credit.getInterestRate().value(),
                        credit.getPaymentMethod(),
                        credit.getStatus(),
                        credit.getCreatedAt(),
                        credit.getUpdatedAt()
                );

        credit.getInstallments()
                .stream()
                .map(i -> installmentMapper.toEntity(i, creditEntity))
                .forEach(creditEntity::addInstallment);

        return creditEntity;
    }

    public Credit toDomain(CreditEntity entity) {
        List<Installment> installments =
                entity.getInstallments()
                        .stream()
                        .map(installmentMapper::toDomain)
                        .toList();

        return new Credit(
                new CreditId(entity.getId()),
                new CustomerId(entity.getCustomer().getId()),
                new Money(entity.getRequestedAmount()),
                installments,
                entity.getCreditType(),
                new InterestRate(entity.getInterestRate()),
                entity.getPaymentMethod(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
