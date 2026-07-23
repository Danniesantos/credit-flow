package com.daniela.creditflow.application.installment.usecase;

import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.application.installment.dto.input.PaymentInstallmentInput;
import com.daniela.creditflow.application.installment.payment.PaymentInput;
import com.daniela.creditflow.application.installment.payment.PaymentResult;
import com.daniela.creditflow.application.installment.payment.PaymentService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.domain.event.InstallmentPaidEvent;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.valueObject.InstallmentId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PayInstallmentUseCase {

    private final CreditRepository creditRepository;
    private final CreditService creditService;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    public PayInstallmentUseCase(
            CreditRepository creditRepository,
            CreditService service,
            PaymentService paymentService,
            ApplicationEventPublisher eventPublisher) {

        this.creditRepository = creditRepository;
        this.creditService = service;
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    public void execute(PaymentInstallmentInput input) {

        Credit credit =
                creditService.findCredit(
                        new CreditId(input.creditId())
                );

        InstallmentId installmentId =
                new InstallmentId(input.installmentId());

        Installment installment =
                credit.findInstallment(installmentId);

        PaymentResult result =
                paymentService.process(
                        input.paymentMethod(),
                        new PaymentInput(
                                installment.getAmount().value()
                        )
                );

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
