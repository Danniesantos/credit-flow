package com.daniela.creditflow.infrastructure.persistence.credit.repository;

import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.credit.mapper.CreditMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CreditRepositoryImpl implements CreditRepository {

    private final CreditJpaRepository jpaRepository;
    private final CreditMapper mapper;

    @Override
    public Credit save(Credit credit) {
        CreditEntity entity = mapper.toEntity(credit);
        CreditEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Page<Credit> findCreditsWithOverdueInstallments(Pageable pageable) {
        return jpaRepository
                .findCreditsWithOverdueInstallments(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Credit> findByIdWithInstallments(CreditId id) {
        return jpaRepository
                .findByIdWithInstallments(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean hasOpenCredits(CustomerId customerId) {
        return jpaRepository.existsByCustomerIdAndStatusIn(
                customerId.value(),
                CreditStatus.openStatuses()
        );
    }

}
