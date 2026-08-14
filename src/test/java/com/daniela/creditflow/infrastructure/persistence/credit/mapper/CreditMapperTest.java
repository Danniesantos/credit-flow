package com.daniela.creditflow.infrastructure.persistence.credit.mapper;

import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.model.CreditType;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.customer.entity.CustomerEntity;
import com.daniela.creditflow.infrastructure.persistence.installment.entity.InstallmentEntity;
import com.daniela.creditflow.infrastructure.persistence.installment.mapper.InstallmentMapper;
import com.daniela.creditflow.support.CreditTestFactory;
import com.daniela.creditflow.support.InstallmentTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditMapperTest {

    @Mock
    private InstallmentMapper installmentMapper;

    @InjectMocks
    private CreditMapper mapper;

    @Test
    @DisplayName("Should map credit to entity")
    void shouldMapCreditToEntity() {

        Credit credit =
                CreditTestFactory.contractedCredit();

        List<Installment> installments =
                credit.getInstallments();

        CreditEntity result =
                mapper.toEntity(credit);

        assertThat(result.getId())
                .isEqualTo(credit.getId().value());

        assertThat(result.getCustomer().getId())
                .isEqualTo(credit.getCustomerId().value());

        assertThat(result.getRequestedAmount())
                .isEqualByComparingTo(
                        credit.getRequestedAmount().value()
                );

        assertThat(result.getCreditType())
                .isEqualTo(credit.getCreditType());

        assertThat(result.getInterestRate())
                .isEqualByComparingTo(
                        credit.getInterestRate().value()
                );

        assertThat(result.getInstallmentsQuantity())
                .isEqualTo(credit.getInstallmentsQuantity());

        assertThat(result.getStatus())
                .isEqualTo(credit.getStatus());

        assertThat(result.getCreatedAt())
                .isEqualTo(credit.getCreatedAt());

        assertThat(result.getUpdatedAt())
                .isEqualTo(credit.getUpdatedAt());

        assertThat(result.getInstallments())
                .hasSize(installments.size());

        verify(installmentMapper, times(installments.size()))
                .toEntity(
                        any(Installment.class),
                        eq(result)
                );
    }

    @Test
    @DisplayName("Should map credit entity to domain")
    void shouldMapCreditEntityToDomain() {

        UUID creditId =
                UUID.randomUUID();

        UUID customerId =
                UUID.randomUUID();

        CreditEntity entity =
                new CreditEntity(
                        creditId,
                        new CustomerEntity(customerId),
                        TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                        CreditType.PERSONAL,
                        TestConstants.FIVE_PERCENT.value(),
                        12,
                        CreditStatus.CONTRACTED,
                        TestConstants.CREATED_AT,
                        TestConstants.CREATED_AT
                );

        Installment installment =
                InstallmentTestFactory.pendingInstallment();

        InstallmentEntity installmentEntity =
                new InstallmentEntity(
                        installment.getId().value(),
                        installment.getNumber(),
                        installment.getAmount().value(),
                        installment.getDueDate(),
                        installment.getPaymentMethod(),
                        installment.getStatus(),
                        installment.getPaidAt(),
                        entity
                );

        entity.addInstallment(installmentEntity);

        when(installmentMapper.toDomain(installmentEntity))
                .thenReturn(installment);

        Credit result =
                mapper.toDomain(entity);

        assertThat(result.getId().value())
                .isEqualTo(creditId);

        assertThat(result.getCustomerId().value())
                .isEqualTo(customerId);

        assertThat(result.getRequestedAmount().value())
                .isEqualByComparingTo(
                        entity.getRequestedAmount()
                );

        assertThat(result.getCreditType())
                .isEqualTo(entity.getCreditType());

        assertThat(result.getInterestRate().value())
                .isEqualByComparingTo(
                        entity.getInterestRate()
                );

        assertThat(result.getInstallmentsQuantity())
                .isEqualTo(entity.getInstallmentsQuantity());

        assertThat(result.getStatus())
                .isEqualTo(entity.getStatus());

        assertThat(result.getCreatedAt())
                .isEqualTo(entity.getCreatedAt());

        assertThat(result.getUpdatedAt())
                .isEqualTo(entity.getUpdatedAt());

        assertThat(result.getInstallments())
                .containsExactly(installment);

        verify(installmentMapper)
                .toDomain(installmentEntity);
    }
}