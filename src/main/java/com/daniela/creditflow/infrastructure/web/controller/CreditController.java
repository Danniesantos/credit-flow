package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.credit.dto.input.CreditAdjustmentInput;
import com.daniela.creditflow.application.credit.dto.input.RequestCreditInput;
import com.daniela.creditflow.application.credit.dto.input.SimulateCreditInput;
import com.daniela.creditflow.application.credit.dto.output.*;
import com.daniela.creditflow.application.credit.usecase.*;
import com.daniela.creditflow.domain.valueobject.CreditId;
import com.daniela.creditflow.infrastructure.web.mapper.CreditWebMapper;
import com.daniela.creditflow.infrastructure.web.request.CreditAdjustmentRequest;
import com.daniela.creditflow.infrastructure.web.request.RequestCreditRequest;
import com.daniela.creditflow.infrastructure.web.request.SimulateCreditRequest;
import com.daniela.creditflow.infrastructure.web.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(
        name = "Credits",
        description = "Operations related to credits"
)
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
    private final FindDebtorsUseCase debtorsUseCase;
    private final ContractCreditUseCase contractUseCase;
    private final CancelCreditUseCase cancelUseCase;
    private final FindCreditOverdueUseCase overdueUseCase;
    private final RenegotiateCreditUseCase renegotiateUseCase;
    private final RestructureCreditUseCase restructureUseCase;

    @Operation(
            summary = "Request credit",
            description = "Creates a new credit request and calculates the applicable interest rate."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Credit successfully requested"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credit request"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Credit request violates a business rule"
            )
    })
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

    @Operation(
            summary = "Simulate credit",
            description = "Simulates a credit based on the requested amount, credit type, and number of installments."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credit simulation successfully calculated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid simulation request"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Credit simulation violates a business rule"
            )
    })
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

    @Operation(
            summary = "Analyze credit",
            description = "Analyzes a credit request based on the customer's credit score, " +
                    "income, and credit limit."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credit successfully analyzed. The response indicates " +
                            "whether the credit was approved or rejected."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit or customer not found"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Credit cannot be analyzed because it is not in the analysis state"
            )
    })
    @PostMapping("/{id}/analyze")
    public ResponseEntity<AnalyzeCreditResponse> analyze(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {
        AnalyzeCreditOutput output =
                analyzeCreditUseCase.execute(
                        creditWebMapper.toCreditId(id));

        return ResponseEntity.ok(
                creditWebMapper.toAnalyzeResponse(output));
    }

    @Operation(
            summary = "Contract credit",
            description = "Contracts an approved credit and generates its installments."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Credit successfully contracted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit not found"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Credit cannot be contracted because it is not approved," +
                            " is already contracted, or has an invalid installment configuration"
            )
    })
    @PostMapping("/{id}/contract")
    public ResponseEntity<Void> contract(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        contractUseCase.execute(creditId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get credit by ID",
            description = "Returns the details of a credit by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credit successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CreditDetailsResponse> findById(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {

        CreditDetailsOutput output =
                findCreditUseCase.execute(
                        creditWebMapper.toCreditId(id));

        return ResponseEntity.ok(
                creditWebMapper.toDetailsResponse(output));
    }

    @Operation(
            summary = "Get credit balance",
            description = "Returns the current balance and payment information of a credit."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credit balance successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit not found"
            )
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> findBalance(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        BalanceOutput output =
                balanceUseCase.execute(creditId);

        return ResponseEntity.ok(
                creditWebMapper.toBalanceResponse(output));

    }

    @Operation(
            summary = "Get overdue installments",
            description = "Returns the overdue status and installments of a credit."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Overdue status successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit not found"
            )
    })
    @GetMapping("/{id}/overdue")
    public ResponseEntity<OverdueResponse> getOverdueStatus(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {
        CreditId creditId =
                creditWebMapper.toCreditId(id);

        OverdueOutput output =
                overdueUseCase.execute(creditId);

        return ResponseEntity.ok(
                creditWebMapper.toOverdueResponse(output));
    }

    @Operation(
            summary = "List debtors",
            description = "Returns a paginated list of customers with overdue credit installments."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Debtors successfully retrieved"
    )
    @GetMapping("/debtors")
    public ResponseEntity<Page<DebtorResponse>> findDebtors(
            @Parameter(description = "Pagination and sorting parameters")
            Pageable pageable) {

        Page<DebtorOutput> output =
                debtorsUseCase.execute(pageable);

        return ResponseEntity.ok(
                output.map(creditWebMapper::toDebtorResponse));
    }

    @Operation(
            summary = "Cancel credit",
            description = "Cancels a credit that is eligible for cancellation."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Credit successfully canceled"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit not found"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Credit cannot be canceled because of its current status"
            )
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        cancelUseCase.execute(creditId);

        return ResponseEntity
                .noContent().build();
    }

    @Operation(
            summary = "Renegotiate credit",
            description = "Renegotiates a credit with overdue installments " +
                    "by generating new installments."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Credit successfully renegotiated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credit adjustment data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit not found"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Credit cannot be renegotiated or the requested " +
                            "installment configuration violates a business rule"
            )
    })
    @PostMapping("/{id}/renegotiate")
    public ResponseEntity<Void> renegotiate(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,
            @RequestBody @Valid CreditAdjustmentRequest request
    ) {

        CreditAdjustmentInput input =
                creditWebMapper.toCreditAdjustmentInput(request);

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        renegotiateUseCase.execute(creditId, input);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Restructure credit",
            description = "Restructures a contracted credit with pending installments " +
                    "by generating a new installment schedule."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Credit successfully restructured"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credit adjustment data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Credit not found"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Credit cannot be restructured or the requested installment " +
                            "configuration violates a business rule"
            )
    })
    @PostMapping("/{id}/restructure")
    public ResponseEntity<Void> restructure(
            @Parameter(
                    description = "Credit unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id,
            @RequestBody @Valid CreditAdjustmentRequest request
    ) {

        CreditAdjustmentInput input =
                creditWebMapper.toCreditAdjustmentInput(request);

        CreditId creditId =
                creditWebMapper.toCreditId(id);

        restructureUseCase.execute(creditId, input);

        return ResponseEntity.noContent().build();
    }
}
