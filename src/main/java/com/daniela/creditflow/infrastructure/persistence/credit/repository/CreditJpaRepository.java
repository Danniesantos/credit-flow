package com.daniela.creditflow.infrastructure.persistence.credit.repository;

import com.daniela.creditflow.domain.model.CreditStatus;
import com.daniela.creditflow.infrastructure.persistence.credit.entity.CreditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query("""
                SELECT c
                FROM CreditEntity c
                WHERE EXISTS (
                    SELECT i
                    FROM InstallmentEntity i
                    WHERE i.credit.id = c.id
                    AND i.status = 'PENDING'
                    AND i.dueDate < CURRENT_DATE
                )
            """)
    Page<CreditEntity> findCreditsWithOverdueInstallments(
            Pageable pageable
    );

    boolean existsByCustomerIdAndStatusIn(
            UUID customerId,
            Collection<CreditStatus> statuses
    );
}
