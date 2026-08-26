package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.service.CreditInstallmentService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.event.CreditRenegotiatedEvent;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueobject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RenegotiateCreditUseCase {

    private final CreditService service;
    private final CreditRepository creditRepository;
    private final CreditInstallmentService creditInstallmentService;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public void execute(
            CreditId creditId,
            CreditAdjustmentInput input) {

        Credit credit =
                service.findCredit(creditId);

        LocalDate today =
                LocalDate.now(clock);

        credit.ensureCanBeRenegotiated(today);

        List<Installment> installments =
                creditInstallmentService.generate(
                        credit,
                        input.installmentsQuantity()
                );

        Instant now =
                clock.instant();

        credit.renegotiate(
                installments,
                today,
                now
        );

        creditRepository.save(credit);

        eventPublisher.publishEvent(
                new CreditRenegotiatedEvent(
                        creditId,
                        credit.getCustomerId(),
                        now
                )
        );
    }
}
