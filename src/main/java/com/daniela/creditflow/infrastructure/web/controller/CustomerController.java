package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.usecase.*;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.infrastructure.web.mapper.CustomerWebMapper;
import com.daniela.creditflow.infrastructure.web.request.CustomerRequest;
import com.daniela.creditflow.infrastructure.web.response.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(
        name = "Customers",
        description = "Operations related to customers"
)
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerWebMapper customerMapper;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final FindCustomerUseCase findCustomerUseCase;
    private final FindAllCustomersUseCase findAllCustomersUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    @Operation(
            summary = "Create customer",
            description = "Creates a new customer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid customer data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Customer CPF or email already exists"
            )
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@RequestBody @Valid
                                                   CustomerRequest request) {

        CreateCustomerInput input = customerMapper.toInput(request);
        CustomerOutput output = createCustomerUseCase.execute(input);
        CustomerResponse response = customerMapper.toResponse(output);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(response);
    }

    @Operation(
            summary = "List customers",
            description = "Returns a paginated list of customers."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customers successfully retrieved"
            )
    })
    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> findAll(
            @Parameter(description = "Page number, starting from 0")
            @PageableDefault(page = 0, size = 10, sort = "name")
            Pageable pageable) {

        Page<CustomerOutput> outputs =
                findAllCustomersUseCase.execute(pageable);

        Page<CustomerResponse> responses =
                outputs.map(customerMapper::toResponse);

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Get customer by ID",
            description = "Returns a customer by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(
            @Parameter(
                    description = "Customer unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {

        CustomerOutput output =
                findCustomerUseCase
                        .execute(customerMapper
                                .toCustomerId(id));

        return ResponseEntity.ok(
                customerMapper.toResponse(output));
    }

    @Operation(
            summary = "Deactivate customer",
            description = "Deactivates an active customer without open credits."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Customer successfully deactivated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Customer cannot be deactivated because it is already inactive or has open credits"
            )
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> deactivateCustomer(
            @Parameter(
                    description = "Customer unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id
    ) {

        CustomerId customerId = customerMapper.toCustomerId(id);

        deactivateCustomerUseCase.execute(customerId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update customer",
            description = "Updates an existing customer."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid customer data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Customer CPF or email already exists"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @Parameter(
                    description = "Customer unique identifier",
                    example = "550e8400-e29b-41d4-a716-446655440000"
            ) @PathVariable UUID id,
            @RequestBody @Valid CustomerRequest request
    ) {

        UpdateCustomerInput input =
                customerMapper.toUpdateInput(id, request);

        CustomerOutput output =
                updateCustomerUseCase.execute(input);

        CustomerResponse response =
                customerMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }
}
