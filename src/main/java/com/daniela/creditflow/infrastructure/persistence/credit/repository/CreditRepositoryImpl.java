package com.daniela.creditflow.infrastructure.persistence.credit.repository;

import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.domain.credit.repository.CreditRepository;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import com.daniela.creditflow.infrastructure.persistence.credit.mapper.CreditMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CreditRepositoryImpl implements CreditRepository {

    private final CreditJpaRepository jpaRepository;
    private final CreditMapper mapper;

    public CreditRepositoryImpl(CreditJpaRepository jpaRepository,
                                CreditMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Credit save(Credit credit) {
        CreditEntity entity = mapper.toEntity(credit);
        CreditEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Credit> findById(CreditId id) {
        return jpaRepository
                .findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Credit> findByIdWithInstallments(CreditId id) {
        return jpaRepository
                .findById(id.value())
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
