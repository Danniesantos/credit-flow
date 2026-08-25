package com.daniela.creditflow.application.credit.usecase;

import com.daniela.creditflow.application.credit.dto.output.OverdueOutput;
import com.daniela.creditflow.application.credit.mapper.CreditApplicationMapper;
import com.daniela.creditflow.application.credit.service.CreditService;
import com.daniela.creditflow.domain.model.Credit;
import com.daniela.creditflow.domain.valueobject.CreditId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FindCreditOverdueUseCase {

    private final CreditService creditService;
    private final CreditApplicationMapper creditMapper;
    private final Clock clock;

    public OverdueOutput execute(CreditId creditId) {

        Credit credit =
                creditService
                        .findCredit(creditId);

        LocalDate today =
                LocalDate.now(clock);

        return creditMapper.toOverdueOutput(
                credit,
                today
        );
    }
}
