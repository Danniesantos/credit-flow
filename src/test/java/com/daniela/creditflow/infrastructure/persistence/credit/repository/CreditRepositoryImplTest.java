package com.daniela.creditflow.infrastructure.persistence.credit.repository;

import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.credit.mapper.CreditMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRepositoryImplTest {

    @Mock
    private CreditJpaRepository jpaRepository;

    @Mock
    private CreditMapper mapper;

    @InjectMocks
    private CreditRepositoryImpl repository;

    @Test
    @DisplayName("Should save credit successfully")
    void shouldSaveCreditSuccessfully() {

        Credit credit = mock(Credit.class);
        CreditEntity entity = mock(CreditEntity.class);
        CreditEntity savedEntity = mock(CreditEntity.class);
        Credit savedCredit = mock(Credit.class);

        when(mapper.toEntity(credit))
                .thenReturn(entity);

        when(jpaRepository.save(entity))
                .thenReturn(savedEntity);

        when(mapper.toDomain(savedEntity))
                .thenReturn(savedCredit);

        Credit result = repository.save(credit);

        assertThat(result)
                .isSameAs(savedCredit);

        verify(mapper)
                .toEntity(credit);

        verify(jpaRepository)
                .save(entity);

        verify(mapper)
                .toDomain(savedEntity);
    }

    @Test
    @DisplayName("Should find credit with installments")
    void shouldFindCreditWithInstallments() {

        CreditId creditId =
                new CreditId(UUID.randomUUID());

        CreditEntity entity = mock(CreditEntity.class);
        Credit credit = mock(Credit.class);

        when(jpaRepository.findByIdWithInstallments(
                creditId.value()))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity))
                .thenReturn(credit);

        Optional<Credit> result =
                repository.findByIdWithInstallments(creditId);

        assertThat(result)
                .isPresent()
                .contains(credit);

        verify(jpaRepository)
                .findByIdWithInstallments(creditId.value());

        verify(mapper)
                .toDomain(entity);
    }

    @Test
    @DisplayName("Should return empty when credit does not exist")
    void shouldReturnEmptyWhenCreditDoesNotExist() {

        CreditId creditId =
                new CreditId(UUID.randomUUID());

        when(jpaRepository.findByIdWithInstallments(
                creditId.value()))
                .thenReturn(Optional.empty());

        Optional<Credit> result =
                repository.findByIdWithInstallments(creditId);

        assertThat(result)
                .isEmpty();

        verify(jpaRepository)
                .findByIdWithInstallments(creditId.value());

        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should find credits with overdue installments")
    void shouldFindCreditsWithOverdueInstallments() {

        Pageable pageable =
                PageRequest.of(0, 10);

        CreditEntity firstEntity =
                mock(CreditEntity.class);

        CreditEntity secondEntity =
                mock(CreditEntity.class);

        Credit firstCredit =
                mock(Credit.class);

        Credit secondCredit =
                mock(Credit.class);

        Page<CreditEntity> entityPage =
                new PageImpl<>(
                        List.of(firstEntity, secondEntity),
                        pageable,
                        2
                );

        when(jpaRepository.findCreditsWithOverdueInstallments(pageable))
                .thenReturn(entityPage);

        when(mapper.toDomain(firstEntity))
                .thenReturn(firstCredit);

        when(mapper.toDomain(secondEntity))
                .thenReturn(secondCredit);

        Page<Credit> result =
                repository.findCreditsWithOverdueInstallments(pageable);

        assertThat(result.getContent())
                .containsExactly(firstCredit, secondCredit);

        assertThat(result.getTotalElements())
                .isEqualTo(2);

        verify(jpaRepository)
                .findCreditsWithOverdueInstallments(pageable);

        verify(mapper)
                .toDomain(firstEntity);

        verify(mapper)
                .toDomain(secondEntity);
    }

    @Test
    @DisplayName("Should return true when customer has open credits")
    void shouldReturnTrueWhenCustomerHasOpenCredits() {

        CustomerId customerId =
                new CustomerId(UUID.randomUUID());

        when(jpaRepository.existsByCustomerIdAndStatusIn(
                customerId.value(),
                CreditStatus.openStatuses()))
                .thenReturn(true);

        boolean result =
                repository.hasOpenCredits(customerId);

        assertThat(result)
                .isTrue();

        verify(jpaRepository)
                .existsByCustomerIdAndStatusIn(
                        customerId.value(),
                        CreditStatus.openStatuses());
    }

    @Test
    @DisplayName("Should return false when customer has no open credits")
    void shouldReturnFalseWhenCustomerHasNoOpenCredits() {

        CustomerId customerId =
                new CustomerId(UUID.randomUUID());

        when(jpaRepository.existsByCustomerIdAndStatusIn(
                customerId.value(),
                CreditStatus.openStatuses()))
                .thenReturn(false);

        boolean result =
                repository.hasOpenCredits(customerId);

        assertThat(result)
                .isFalse();

        verify(jpaRepository)
                .existsByCustomerIdAndStatusIn(
                        customerId.value(),
                        CreditStatus.openStatuses());
    }
}