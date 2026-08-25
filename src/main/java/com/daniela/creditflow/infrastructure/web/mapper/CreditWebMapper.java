package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.*;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.infrastructure.web.request.CreditAdjustmentRequest;
import com.daniela.creditflow.infrastructure.web.request.RequestCreditRequest;
import com.daniela.creditflow.infrastructure.web.request.SimulateCreditRequest;
import com.daniela.creditflow.infrastructure.web.response.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CreditWebMapper {

    private final InstallmentWebMapper installmentMapper;

    public CreditWebMapper(InstallmentWebMapper installmentMapper) {
        this.installmentMapper = installmentMapper;
    }

    public RequestCreditInput toInput(RequestCreditRequest request) {
        return new RequestCreditInput(
                request.getCustomerId(),
                request.getRequestedAmount(),
                request.getInstallments(),
                request.getCreditType());
    }

    public SimulateCreditInput toSimulateInput(SimulateCreditRequest request) {
        return new SimulateCreditInput(
                request.getRequestedAmount(),
                request.getInstallments(),
                request.getCreditType());
    }

    public CreditId toCreditId(UUID id) {
        return new CreditId(id);
    }

    public RequestCreditResponse toRequestResponse(RequestCreditOutput output) {

        return new RequestCreditResponse(
                output.creditId(),
                output.customerId(),
                output.requestedAmount(),
                output.installments(),
                output.interestRate(),
                output.creditType(),
                output.status(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    public SimulateCreditResponse toSimulateResponse(SimulateCreditOutput output) {

        return new SimulateCreditResponse(
                output.requestedAmount(),
                output.interestRate(),
                output.totalAmount(),
                output.installments(),
                output.installmentAmount()
        );
    }

    public AnalyzeCreditResponse toAnalyzeResponse(AnalyzeCreditOutput output) {

        return new AnalyzeCreditResponse(
                output.creditId(),
                output.status(),
                output.reason()
        );
    }

    public CreditDetailsResponse toDetailsResponse(CreditDetailsOutput output) {

        List<InstallmentDetailsResponse> installments =
                output.installments()
                        .stream()
                        .map(installmentMapper::toResponse)
                        .toList();

        return new CreditDetailsResponse(
                output.creditId(),
                output.customerId(),
                output.requestedAmount(),
                output.interestRate(),
                output.creditType(),
                output.status(),
                installments,
                output.createdAt(),
                output.updatedAt()
        );
    }

    public BalanceResponse toBalanceResponse(BalanceOutput output) {

        return new BalanceResponse(
                output.totalContractAmount(),
                output.paidAmount(),
                output.remainingAmount(),
                output.remainingInstallments()
        );
    }

    public OverdueResponse toOverdueResponse(OverdueOutput output) {

        List<OverdueInstallmentResponse> installments =
                output.installments()
                        .stream()
                        .map(installmentMapper::toOverdueInstallmentResponse)
                        .toList();

        return new OverdueResponse(
                output.hasOverdueInstallments(),
                output.overdueInstallmentsQuantity(),
                output.overdueAmount(),
                installments
        );
    }

    public DebtorResponse toDebtorResponse(DebtorOutput output) {

        return new DebtorResponse(
                output.creditId(),
                output.customerId(),
                output.overdueInstallments(),
                output.overdueAmount()
        );
    }

    public CreditAdjustmentInput toCreditAdjustmentInput(CreditAdjustmentRequest request) {
        return new CreditAdjustmentInput(request.getInstallmentsQuantity());
    }
}
