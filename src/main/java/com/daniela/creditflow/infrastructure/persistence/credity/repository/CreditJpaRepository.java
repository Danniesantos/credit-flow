package com.daniela.creditflow.infrastructure.persistence.credity.repository;

import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.infrastructure.persistence.credity.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface CreditJpaRepository extends JpaRepository<CreditEntity, UUID> {

    boolean existsByCustomerIdAndStatusIn(UUID customerId, Collection<CreditStatus> statuses);
}
