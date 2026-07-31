package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.*;
import com.daniela.creditflow.application.credit.usecase.*;
import com.daniela.creditflow.domain.valueObject.CreditId;
import com.daniela.creditflow.infrastructure.web.mapper.CreditWebMapper;
import com.daniela.creditflow.infrastructure.web.request.CreditAdjustmentRequest;
import com.daniela.creditflow.infrastructure.web.request.RequestCreditRequest;
import com.daniela.creditflow.infrastructure.web.request.SimulateCreditRequest;
import com.daniela.creditflow.infrastructure.web.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/credits")
public class CreditController {

    private final CreditWebMapper creditWebMapper;
    private final SimulateCreditUseCase simulateCreditUseCase;
    private final RequestCreditUseCase requestCreditUseCase;
    private final AnalyzeCreditUseCase analyzeCreditUseCase;
    private final FindCreditUseCase findCreditUseCase;
    private final FindCreditBalanceUseCase balanceUseCase;
    private final ContractCreditUseCase contractUseCase;
    private final CancelCreditUseCase cancelUseCase;
    private final FindCreditOverdueUseCase overdueUseCase;
    private final RenegotiateCreditUseCase renegotiateUseCase;
    private final RestructureCreditUseCase restructureUseCase;

    @PostMapping
    public ResponseEntity<RequestCreditResponse> request(@RequestBody @Valid
                                                         RequestCreditRequest request) {
        RequestCreditInput input =
                creditWebMapper.toInput(request);

        RequestCreditOutput output =
                requestCreditUseCase.execute(input);

        RequestCreditResponse response =
                creditWebMapper.toRequestResponse(output);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.creditId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(response);
    }

    @PostMapping("/simulate")
    public ResponseEntity<SimulateCreditResponse> simulate(@RequestBody @Valid
                                                           SimulateCreditRequest request) {
        SimulateCreditInput input =
                creditWebMapper.toSimulateInput(request);

        SimulateCreditOutput output =
                simulateCreditUseCase.execute(input);

        return ResponseEntity.ok(
                creditWebMapper.toSimulateResponse(output));
    }

    @PostMapping("/{id}/analyze")
    public ResponseEntity<AnalyzeCreditResponse> analyze(@PathVariable
                                                         UUID id) {
        AnalyzeCreditOutput output =
                analyzeCreditUseCase.execute(
                        creditWebMapper.toCreditId(id));

        return ResponseEntity.ok(
                creditWebMapper.toAnalyzeResponse(output));
    }

    @PostMapping("/{id}/contract")
    public ResponseEntity<Void> contract(@PathVariable
                                         UUID id) {

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        contractUseCase.execute(creditId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditDetailsResponse> findById(@PathVariable
                                                          UUID id) {

        CreditDetailsOutput output =
                findCreditUseCase.execute(
                        creditWebMapper.toCreditId(id));

        return ResponseEntity.ok(
                creditWebMapper.toDetailsResponse(output));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> findBalance(@PathVariable
                                                       UUID id) {

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        BalanceOutput output =
                balanceUseCase.execute(creditId);

        return ResponseEntity.ok(
                creditWebMapper.toBalanceResponse(output));

    }

    @GetMapping("/{id}/overdue")
    public ResponseEntity<OverdueResponse> getOverdueStatus(@PathVariable
                                                            UUID id) {
        CreditId creditId =
                creditWebMapper.toCreditId(id);

        OverdueOutput output =
                overdueUseCase.execute(creditId);

        return ResponseEntity.ok(
                creditWebMapper.toOverdueResponse(output));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable
                                       UUID id) {

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        cancelUseCase.execute(creditId);

        return ResponseEntity
                .noContent().build();
    }

    @PostMapping("{id}/renegotiate")
    public ResponseEntity<Void> renegotiate(@PathVariable UUID id,
                                            @RequestBody CreditAdjustmentRequest request) {

        CreditAdjustmentInput input =
                creditWebMapper.toCreditAdjustmentInput(request);

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        renegotiateUseCase.execute(creditId, input);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/restructure")
    public ResponseEntity<Void> restructure(@PathVariable UUID id,
                                            @RequestBody CreditAdjustmentRequest request) {

        CreditAdjustmentInput input =
                creditWebMapper.toCreditAdjustmentInput(request);

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        restructureUseCase.execute(creditId, input);

        return ResponseEntity.noContent().build();
    }
}
