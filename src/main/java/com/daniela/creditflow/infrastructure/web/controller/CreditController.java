package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.AnalyzeCreditOutput;
import com.daniela.creditflow.application.credit.dto.output.CreditDetailsOutput;
import com.daniela.creditflow.application.credit.dto.output.RequestCreditOutput;
import com.daniela.creditflow.application.credit.dto.output.SimulateCreditOutput;
import com.daniela.creditflow.application.credit.usecase.AnalyzeCreditUseCase;
import com.daniela.creditflow.application.credit.usecase.FindCreditUseCase;
import com.daniela.creditflow.application.credit.usecase.RequestCreditUseCase;
import com.daniela.creditflow.application.credit.usecase.SimulateCreditUseCase;
import com.daniela.creditflow.infrastructure.web.mapper.CreditWebMapper;
import com.daniela.creditflow.infrastructure.web.request.RequestCreditRequest;
import com.daniela.creditflow.infrastructure.web.request.SimulateCreditRequest;
import com.daniela.creditflow.infrastructure.web.response.AnalyzeCreditResponse;
import com.daniela.creditflow.infrastructure.web.response.CreditDetailsResponse;
import com.daniela.creditflow.infrastructure.web.response.RequestCreditResponse;
import com.daniela.creditflow.infrastructure.web.response.SimulateCreditResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/credits")
public class CreditController {

    private final CreditWebMapper creditWebMapper;
    private final SimulateCreditUseCase simulateCreditUseCase;
    private final RequestCreditUseCase requestCreditUseCase;
    private final AnalyzeCreditUseCase analyzeCreditUseCase;
    private final FindCreditUseCase findCreditUseCase;


    public CreditController(CreditWebMapper creditWebMapper,
                            SimulateCreditUseCase simulateCreditUseCase,
                            RequestCreditUseCase requestCreditUseCase,
                            AnalyzeCreditUseCase analyzeCreditUseCase,
                            FindCreditUseCase findCredit) {

        this.creditWebMapper = creditWebMapper;
        this.simulateCreditUseCase = simulateCreditUseCase;
        this.requestCreditUseCase = requestCreditUseCase;
        this.analyzeCreditUseCase = analyzeCreditUseCase;
        this.findCreditUseCase = findCredit;
    }


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
                analyzeCreditUseCase.execute(id);

        return ResponseEntity.ok(
                creditWebMapper.toAnalyzeResponse(output));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditDetailsResponse> findById(@PathVariable
                                                          UUID id) {

        CreditDetailsOutput output =
                findCreditUseCase.execute(id);

        CreditDetailsResponse response =
                creditWebMapper.toDetailsResponse(output);

        return ResponseEntity.ok(response);
    }

}
