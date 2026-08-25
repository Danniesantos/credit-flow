package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.BalanceOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueobject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindCreditBalanceUseCase {

    private final CreditService creditService;
    private final CreditApplicationMapper creditMapper;

    public BalanceOutput execute(CreditId creditId) {

        Credit credit =
                creditService
                        .findCredit(creditId);

        return creditMapper.toBalanceOutput(credit);
    }
}
