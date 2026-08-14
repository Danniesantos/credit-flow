package com.daniela.creditflow.infrastructure.persistence.installment.mapper;

import com.daniela.creditflow.domain.model.*;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.installment.entity.InstallmentEntity;
import com.daniela.creditflow.support.InstallmentTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class InstallmentMapperTest {

    private final InstallmentMapper mapper =
            new InstallmentMapper();

    @Test
    @DisplayName("Should map installment to entity")
    void shouldMapInstallmentToEntity() {

        Installment installment =
                InstallmentTestFactory.paidInstallment();

        CreditEntity credit =
                creditEntity(installment.getCreditId().value());

        InstallmentEntity result =
                mapper.toEntity(
                        installment,
                        credit
                );

        assertThat(result.getId())
                .isEqualTo(installment.getId().value());

        assertThat(result.getNumber())
                .isEqualTo(installment.getNumber());

        assertThat(result.getAmount())
                .isEqualByComparingTo(
                        installment.getAmount().value()
                );

        assertThat(result.getDueDate())
                .isEqualTo(installment.getDueDate());

        assertThat(result.getPaymentMethod())
                .isEqualTo(installment.getPaymentMethod());

        assertThat(result.getStatus())
                .isEqualTo(installment.getStatus());

        assertThat(result.getPaidAt())
                .isEqualTo(installment.getPaidAt());

        assertThat(result.getCredit())
                .isEqualTo(credit);
    }

    @Test
    @DisplayName("Should map installment entity to domain")
    void shouldMapInstallmentEntityToDomain() {

        UUID installmentId =
                UUID.randomUUID();

        UUID creditId =
                UUID.randomUUID();

        CreditEntity credit =
                creditEntity(creditId);

        InstallmentEntity entity =
                new InstallmentEntity(
                        installmentId,
                        TestConstants.INSTALLMENT_NUMBER,
                        TestConstants.INSTALLMENT_AMOUNT.value(),
                        TestConstants.TEST_DATE,
                        PaymentMethod.PIX,
                        InstallmentStatus.PAID,
                        TestConstants.PAID_AT,
                        credit
                );

        Installment result =
                mapper.toDomain(entity);

        assertThat(result.getId().value())
                .isEqualTo(installmentId);

        assertThat(result.getNumber())
                .isEqualTo(entity.getNumber());

        assertThat(result.getAmount().value())
                .isEqualByComparingTo(entity.getAmount());

        assertThat(result.getDueDate())
                .isEqualTo(entity.getDueDate());

        assertThat(result.getPaymentMethod())
                .isEqualTo(entity.getPaymentMethod());

        assertThat(result.getStatus())
                .isEqualTo(entity.getStatus());

        assertThat(result.getCreditId().value())
                .isEqualTo(creditId);

        assertThat(result.getPaidAt())
                .isEqualTo(entity.getPaidAt());
    }

    private CreditEntity creditEntity(UUID id) {

        return new CreditEntity(
                id,
                null,
                TestConstants.TOTAL_CREDIT_AMOUNT.value(),
                CreditType.PERSONAL,
                TestConstants.FIVE_PERCENT.value(),
                12,
                CreditStatus.APPROVED,
                Instant.now(),
                Instant.now()
        );
    }
}