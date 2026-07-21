package com.daniela.creditflow.domain.credit.repository;

import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.customer.valueObject.CustomerId;

import java.util.Optional;

public interface CreditRepository {

    Credit save(Credit credit);

    Optional<Credit> findById(CreditId id);

    Optional<Credit>findByIdWithInstallments(CreditId id);

    boolean hasOpenCredits(CustomerId customerId);

}
