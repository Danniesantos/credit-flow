package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.service.CreditInstallmentService;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.model.Installment;
import com.daniela.creditflow.domain.repository.CreditRepository;
import com.daniela.creditflow.domain.valueObject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RenegotiateCreditUseCase {

    private final CreditService service;
    private final CreditRepository creditRepository;
    private final CreditInstallmentService creditInstallmentService;

    @Transactional
    public void execute(CreditId creditId,
                        CreditAdjustmentInput input) {

        Credit credit = service.findCredit(creditId);

        List<Installment> installments =
                creditInstallmentService.generate(
                        credit,
                        input.installmentsQuantity()
                );

        credit.renegotiate(installments);

        creditRepository.save(credit);
    }
}
