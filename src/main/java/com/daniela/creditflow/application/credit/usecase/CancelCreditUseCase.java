package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.event.CreditCanceledEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueObject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CancelCreditUseCase {

    private final CreditService creditService;
    private final CreditRepository creditRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(CreditId creditId) {

        Credit credit = creditService.findCredit(creditId);

        credit.cancel();

        creditRepository.save(credit);

        eventPublisher.publishEvent(
                new CreditCanceledEvent(
                        credit.getId(),
                        credit.getCustomerId(),
                        Instant.now()
                )
        );
    }
}
