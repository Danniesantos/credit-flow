package com.daniela.creditflow.infrastructure.persistence.credit.repository;

import com.daniela.creditflow.domain.credit.model.CreditStatus;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CreditJpaRepository extends JpaRepository<CreditEntity, UUID> {

    @Query("""
                select c
                from CreditEntity c
                left join fetch c.installments
                where c.id = :id
            """)
    Optional<CreditEntity> findByIdWithInstallments(
            @Param("id") UUID id
    );

    boolean existsByCustomerIdAndStatusIn(
            UUID customerId,
            Collection<CreditStatus> statuses
    );

}
