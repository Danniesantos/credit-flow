package com.daniela.creditflow.domain.repository;

import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CreditRepository {

    Credit save(Credit credit);

    Page<Credit> findCreditsWithOverdueInstallments(Pageable pageable);

    Optional<Credit>findByIdWithInstallments(CreditId id);

    boolean hasOpenCredits(CustomerId customerId);

}
