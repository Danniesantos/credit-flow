package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.event.CreditCanceledEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CancelCreditUseCase {

    private final CreditService creditService;
    private final CreditRepository creditRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public void execute(CreditId creditId) {

        Credit credit = creditService.findCredit(creditId);

        Instant now =
                clock.instant();

        credit.cancel(now);

        creditRepository.save(credit);

        eventPublisher.publishEvent(
                new CreditCanceledEvent(
                        credit.getId(),
                        credit.getCustomerId(),
                        now
                )
        );
    }
}
