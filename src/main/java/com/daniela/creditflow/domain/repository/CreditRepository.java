package com.daniela.creditflow.domain.repository;

import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.valueObject.CustomerId;

import java.util.Optional;

public interface CreditRepository {

    Credit save(Credit credit);

    Optional<Credit> findById(CreditId id);

    Optional<Credit>findByIdWithInstallments(CreditId id);

    boolean hasOpenCredits(CustomerId customerId);

}
