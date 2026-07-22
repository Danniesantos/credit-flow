package com.daniela.creditflow.application.installment.usecase;

import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.application.installment.payment.strategy.PaymentStrategy;
import com.daniela.creditflow.application.installment.payment.PaymentStrategyFactory;
import com.daniela.creditflow.domain.credit.model.Credit;
import com.daniela.creditflow.domain.credit.repository.CreditRepository;
import com.daniela.creditflow.domain.credit.valueObject.CreditId;
import com.daniela.creditflow.domain.exceptions.DomainException;
import com.daniela.creditflow.domain.installment.event.InstallmentPaidEvent;
import com.daniela.creditflow.domain.installment.valueObject.InstallmentId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PayInstallmentUseCase {

    private final CreditRepository creditRepository;
    private final CreditService service;
    private final PaymentStrategyFactory paymentFactory;
    private final ApplicationEventPublisher eventPublisher;

    public PayInstallmentUseCase(
            CreditRepository creditRepository,
            CreditService service,
            PaymentStrategyFactory paymentFactory,
            ApplicationEventPublisher eventPublisher) {

        this.creditRepository = creditRepository;
        this.service = service;
        this.paymentFactory = paymentFactory;
        this.eventPublisher = eventPublisher;
    }

    public void execute(PaymentInstallmentInput input) {

        Credit credit =
                service.findCredit(
                        new CreditId(input.creditId())
                );

        InstallmentId installmentId =
                new InstallmentId(input.installmentId());

        BigDecimal amount =
                credit.installmentAmount(installmentId)
                        .value();

        PaymentStrategy strategy =
                paymentFactory.get(
                        input.paymentMethod()
                );


        PaymentResult result =
                strategy.process(
                        new PaymentInput(amount)
                );


        if (!result.success()) {
            throw new DomainException(
                    "Payment failed"
            );
        }

        credit.markInstallmentAsPaid(
                installmentId,
                input.paymentMethod(),
                result.paidAt()
        );


        creditRepository.save(credit);

        eventPublisher.publishEvent(
                new InstallmentPaidEvent(
                        credit.getId(),
                        installmentId,
                        credit.getCustomerId(),
                        result.paidAt()
                )
        );
    }
}
