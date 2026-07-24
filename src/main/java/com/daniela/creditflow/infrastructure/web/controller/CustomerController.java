package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.usecase.*;
import com.daniela.creditflow.domain.valueObject.CustomerId;
import com.daniela.creditflow.infrastructure.web.mapper.CustomerWebMapper;
import com.daniela.creditflow.infrastructure.web.request.CustomerRequest;
import com.daniela.creditflow.infrastructure.web.response.CustomerResponse;
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

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> findAll(@PageableDefault(page = 0, size = 10, sort = "name")
                                                          Pageable pageable) {

        Page<CustomerOutput> outputs =
                findAllCustomersUseCase.execute(pageable);

        Page<CustomerResponse> responses =
                outputs.map(customerMapper::toResponse);

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable
                                                     UUID id) {

        CustomerOutput output =
                findCustomerUseCase
                        .execute(customerMapper
                                .toCustomerId(id));

        return ResponseEntity.ok(
                customerMapper.toResponse(output));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable
                                                   UUID id) {

        CustomerId customerId = customerMapper.toCustomerId(id);

        deactivateCustomerUseCase.execute(customerId);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable UUID id,
                                                   @RequestBody @Valid
                                                   CustomerRequest request) {

        UpdateCustomerInput input =
                customerMapper.updateToInput(id, request);

        CustomerOutput output =
                updateCustomerUseCase.execute(input);

        CustomerResponse response =
                customerMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }
}
