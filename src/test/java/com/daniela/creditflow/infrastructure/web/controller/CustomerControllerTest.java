package com.daniela.creditflow.infrastructure.web.controller;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.application.customer.usecase.*;
import com.daniela.creditflow.domain.exceptions.CpfAlreadyExistsException;
import com.daniela.creditflow.domain.exceptions.CustomerAlreadyInactiveException;
import com.daniela.creditflow.domain.exceptions.CustomerNotFoundException;
import com.daniela.creditflow.domain.exceptions.EmailAlreadyExistsException;
import com.daniela.creditflow.domain.model.CustomerStatus;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.infrastructure.web.mapper.CustomerWebMapper;
import com.daniela.creditflow.infrastructure.web.request.CustomerRequest;
import com.daniela.creditflow.infrastructure.web.response.CustomerResponse;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerWebMapper customerMapper;
    @MockitoBean
    private CreateCustomerUseCase createCustomerUseCase;
    @MockitoBean
    private FindCustomerUseCase findCustomerUseCase;
    @MockitoBean
    private FindAllCustomersUseCase findAllCustomersUseCase;
    @MockitoBean
    private DeactivateCustomerUseCase deactivateCustomerUseCase;
    @MockitoBean
    private UpdateCustomerUseCase updateCustomerUseCase;

    @ParameterizedTest
    @MethodSource("invalidCustomerRequests")
    @DisplayName("Should return bad request when customer request is invalid")
    void shouldReturnBadRequestWhenCustomerRequestIsInvalid(
            String requestBody) throws Exception {

        mockMvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerMapper);
        verifyNoInteractions(createCustomerUseCase);
    }

    private static Stream<String> invalidCustomerRequests() {

        return Stream.of(
                """
                {
                  "name": "Ab",
                  "cpf": "292.462.720-64",
                  "email": "testando@email.com",
                  "dateOfBirth": "1992-01-10",
                  "phoneNumber": "19999999999",
                  "monthlyIncome": 5000,
                  "creditScore": 800
                }
                """,

                """
                {
                  "name": "Daniela Santos",
                  "cpf": "292.462.720-64",
                  "email": "testando@email.com",
                  "dateOfBirth": "2027-01-01",
                  "phoneNumber": "19999999999",
                  "monthlyIncome": 5000,
                  "creditScore": 800
                }
                """,

                """
                {
                  "name": "Daniela Santos",
                  "cpf": "292.462.720-64",
                  "email": "testando@email.com",
                  "dateOfBirth": "1992-01-10",
                  "phoneNumber": "19999999999",
                  "monthlyIncome": 0,
                  "creditScore": 800
                }
                """,

                """
                {
                  "name": "Daniela Santos",
                  "cpf": "292.462.720-64",
                  "email": "testando@email.com",
                  "dateOfBirth": "1992-01-10",
                  "phoneNumber": "19999999999",
                  "monthlyIncome": 5000,
                  "creditScore": 1001
                }
                """,

                """
                {
                  "name": "",
                  "cpf": "292.462.720-64",
                  "email": "testando@email.com",
                  "dateOfBirth": "1992-01-10",
                  "phoneNumber": "19999999999",
                  "monthlyIncome": 5000,
                  "creditScore": 800
                }
                """
        );
    }
    @Test
    @DisplayName("Should create customer successfully")
    void shouldCreateCustomerSuccessfully() throws Exception {

        CustomerOutput output =
                customerOutput(UUID.randomUUID());

        CustomerResponse response =
                customerResponse(output);

        CreateCustomerInput input =
                new CreateCustomerInput(
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800
                );

        when(customerMapper.toInput(any(CustomerRequest.class)))
                .thenReturn(input);

        when(createCustomerUseCase.execute(input))
                .thenReturn(output);

        when(customerMapper.toResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "%s",
                                          "cpf": "292.462.720-64",
                                          "email": "testando@email.com",
                                          "dateOfBirth": "%s",
                                          "phoneNumber": "19999999999",
                                          "monthlyIncome": %s,
                                          "creditScore": 800
                                        }
                                        """.formatted(
                                        TestConstants.CUSTOMER_NAME,
                                        TestConstants.CUSTOMER_BIRTH_DATE,
                                        TestConstants.CUSTOMER_MONTHLY_INCOME.value()
                                ))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        org.hamcrest.Matchers.endsWith(
                                "/customers/" + output.id())))
                .andExpect(jsonPath("$.id")
                        .value(output.id().toString()))
                .andExpect(jsonPath("$.name")
                        .value(output.name()))
                .andExpect(jsonPath("$.cpf")
                        .value(output.cpf()))
                .andExpect(jsonPath("$.email")
                        .value(output.email()))
                .andExpect(jsonPath("$.creditScore")
                        .value(output.creditScore()))
                .andExpect(jsonPath("$.status")
                        .value(output.status().name()));

        verify(customerMapper)
                .toInput(any(CustomerRequest.class));

        verify(createCustomerUseCase)
                .execute(input);

        verify(customerMapper)
                .toResponse(output);
    }

    @Test
    @DisplayName("Should find customer by id successfully")
    void shouldFindCustomerByIdSuccessfully() throws Exception {

        UUID customerId = UUID.randomUUID();

        CustomerOutput output =
                customerOutput(customerId);

        CustomerResponse response =
                customerResponse(output);

        when(customerMapper.toCustomerId(customerId))
                .thenReturn(new CustomerId(customerId));

        when(findCustomerUseCase.execute(
                new CustomerId(customerId)
        )).thenReturn(output);

        when(customerMapper.toResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        get("/customers/{id}", customerId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(customerId.toString()))
                .andExpect(jsonPath("$.name")
                        .value(output.name()))
                .andExpect(jsonPath("$.cpf")
                        .value(output.cpf()))
                .andExpect(jsonPath("$.email")
                        .value(output.email()))
                .andExpect(jsonPath("$.creditScore")
                        .value(output.creditScore()))
                .andExpect(jsonPath("$.status")
                        .value(output.status().name()));

        verify(customerMapper)
                .toCustomerId(customerId);

        verify(findCustomerUseCase)
                .execute(new CustomerId(customerId));

        verify(customerMapper)
                .toResponse(output);
    }

    @Test
    @DisplayName("Should find all customers successfully")
    void shouldFindAllCustomersSuccessfully() throws Exception {

        CustomerOutput first =
                customerOutput(UUID.randomUUID());

        CustomerOutput second =
                customerOutput(UUID.randomUUID());

        Page<CustomerOutput> outputPage =
                new PageImpl<>(
                        List.of(first, second)
                );

        CustomerResponse firstResponse =
                customerResponse(first);

        CustomerResponse secondResponse =
                customerResponse(second);

        when(findAllCustomersUseCase.execute(any(Pageable.class)))
                .thenReturn(outputPage);

        when(customerMapper.toResponse(first))
                .thenReturn(firstResponse);

        when(customerMapper.toResponse(second))
                .thenReturn(secondResponse);

        mockMvc.perform(
                        get("/customers")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()")
                        .value(2))
                .andExpect(jsonPath("$.content[0].id")
                        .value(first.id().toString()))
                .andExpect(jsonPath("$.content[0].name")
                        .value(first.name()))
                .andExpect(jsonPath("$.content[1].id")
                        .value(second.id().toString()))
                .andExpect(jsonPath("$.content[1].name")
                        .value(second.name()));

        verify(findAllCustomersUseCase)
                .execute(any(Pageable.class));

        verify(customerMapper)
                .toResponse(first);

        verify(customerMapper)
                .toResponse(second);
    }

    @Test
    @DisplayName("Should deactivate customer successfully")
    void shouldDeactivateCustomerSuccessfully() throws Exception {

        UUID customerId = UUID.randomUUID();

        CustomerId input = new CustomerId(customerId);

        when(customerMapper.toCustomerId(customerId))
                .thenReturn(input);

        doNothing()
                .when(deactivateCustomerUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/customers/{id}/status", customerId)
                )
                .andExpect(status().isNoContent());

        verify(customerMapper)
                .toCustomerId(customerId);

        verify(deactivateCustomerUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return not found when customer does not exist")
    void shouldReturnNotFoundWhenCustomerDoesNotExist()
            throws Exception {

        UUID customerId = UUID.randomUUID();

        CustomerId input = new CustomerId(customerId);

        when(customerMapper.toCustomerId(customerId))
                .thenReturn(input);

        doThrow(new CustomerNotFoundException())
                .when(deactivateCustomerUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/customers/{id}/status", customerId)
                )
                .andExpect(status().isNotFound());

        verify(deactivateCustomerUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return not found when customer does not exist")
    void shouldReturnNotFoundWhenCustomerDoesNotExistOnFindById()
            throws Exception {

        UUID customerId = UUID.randomUUID();

        CustomerId input = new CustomerId(customerId);

        when(customerMapper.toCustomerId(customerId))
                .thenReturn(input);

        when(findCustomerUseCase.execute(input))
                .thenThrow(new CustomerNotFoundException());

        mockMvc.perform(
                        get("/customers/{id}", customerId)
                )
                .andExpect(status().isNotFound());

        verify(customerMapper)
                .toCustomerId(customerId);

        verify(findCustomerUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return unprocessable entity when customer is already inactive")
    void shouldReturnUnprocessableEntityWhenCustomerIsAlreadyInactive()
            throws Exception {

        UUID customerId = UUID.randomUUID();

        CustomerId input = new CustomerId(customerId);

        when(customerMapper.toCustomerId(customerId))
                .thenReturn(input);

        doThrow(new CustomerAlreadyInactiveException())
                .when(deactivateCustomerUseCase)
                .execute(input);

        mockMvc.perform(
                        patch("/customers/{id}/status", customerId)
                )
                .andExpect(status().isUnprocessableEntity());

        verify(deactivateCustomerUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return empty page when there are no customers")
    void shouldReturnEmptyPageWhenThereAreNoCustomers()
            throws Exception {

        when(findAllCustomersUseCase.execute(any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(
                        get("/customers")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(findAllCustomersUseCase)
                .execute(any(Pageable.class));
    }

    @Test
    @DisplayName("Should update customer successfully")
    void shouldUpdateCustomerSuccessfully() throws Exception {

        UUID customerId = UUID.randomUUID();

        UpdateCustomerInput input =
                new UpdateCustomerInput(
                        customerId,
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800
                );

        CustomerOutput output =
                customerOutput(customerId);

        CustomerResponse response =
                customerResponse(output);

        when(customerMapper.toUpdateInput(
                eq(customerId),
                any(CustomerRequest.class)
        )).thenReturn(input);

        when(updateCustomerUseCase.execute(input))
                .thenReturn(output);

        when(customerMapper.toResponse(output))
                .thenReturn(response);

        mockMvc.perform(
                        put("/customers/{id}", customerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "%s",
                                          "cpf": "292.462.720-64",
                                          "email": "testando@email.com",
                                          "dateOfBirth": "%s",
                                          "phoneNumber": "19999999999",
                                          "monthlyIncome": %s,
                                          "creditScore": 800
                                        }
                                        """.formatted(
                                        TestConstants.CUSTOMER_NAME,
                                        TestConstants.CUSTOMER_BIRTH_DATE,
                                        TestConstants.CUSTOMER_MONTHLY_INCOME.value()
                                ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(customerId.toString()))
                .andExpect(jsonPath("$.name")
                        .value(output.name()))
                .andExpect(jsonPath("$.cpf")
                        .value(output.cpf()))
                .andExpect(jsonPath("$.email")
                        .value(output.email()))
                .andExpect(jsonPath("$.creditScore")
                        .value(output.creditScore()))
                .andExpect(jsonPath("$.status")
                        .value(output.status().name()));

        verify(customerMapper)
                .toUpdateInput(
                        eq(customerId),
                        any(CustomerRequest.class)
                );

        verify(updateCustomerUseCase)
                .execute(input);

        verify(customerMapper)
                .toResponse(output);
    }

    @Test
    @DisplayName("Should return bad request when customer id is invalid")
    void shouldReturnBadRequestWhenCustomerIdIsInvalidOnUpdate()
            throws Exception {

        mockMvc.perform(
                        put("/customers/{id}", "invalid-uuid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Testando",
                                          "cpf": "292.462.720-64",
                                          "email": "testando@email.com",
                                          "dateOfBirth": "1992-01-10",
                                          "phoneNumber": "19999999999",
                                          "monthlyIncome": 5000,
                                          "creditScore": 800
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerMapper);
        verifyNoInteractions(updateCustomerUseCase);
    }

    @Test
    @DisplayName("Should return not found when customer does not exist")
    void shouldReturnNotFoundWhenCustomerDoesNotExistOnUpdate()
            throws Exception {

        UUID customerId = UUID.randomUUID();

        UpdateCustomerInput input =
                new UpdateCustomerInput(
                        customerId,
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800
                );

        when(customerMapper.toUpdateInput(
                eq(customerId),
                any(CustomerRequest.class)
        )).thenReturn(input);

        when(updateCustomerUseCase.execute(input))
                .thenThrow(new CustomerNotFoundException());

        mockMvc.perform(
                        put("/customers/{id}", customerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Testando",
                                          "cpf": "292.462.720-64",
                                          "email": "testando@email.com",
                                          "dateOfBirth": "1992-01-10",
                                          "phoneNumber": "19999999999",
                                          "monthlyIncome": 5000,
                                          "creditScore": 800
                                        }
                                        """)
                )
                .andExpect(status().isNotFound());

        verify(customerMapper)
                .toUpdateInput(
                        eq(customerId),
                        any(CustomerRequest.class)
                );

        verify(updateCustomerUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return bad request when update request is invalid")
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {

        UUID customerId = UUID.randomUUID();

        mockMvc.perform(
                        put("/customers/{id}", customerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "",
                                          "cpf": "292.462.720-64",
                                          "email": "testando@email.com",
                                          "dateOfBirth": "1992-01-10",
                                          "phoneNumber": "19999999999",
                                          "monthlyIncome": 5000,
                                          "creditScore": 800
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerMapper);
        verifyNoInteractions(updateCustomerUseCase);
    }

    @Test
    @DisplayName("Should return conflict when CPF already exists")
    void shouldReturnConflictWhenCpfAlreadyExists()
            throws Exception {

        CreateCustomerInput input =
                new CreateCustomerInput(
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800
                );

        when(customerMapper.toInput(any(CustomerRequest.class)))
                .thenReturn(input);

        when(createCustomerUseCase.execute(input))
                .thenThrow(new CpfAlreadyExistsException());

        mockMvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Testando",
                                          "cpf": "292.462.720-64",
                                          "email": "testando@email.com",
                                          "dateOfBirth": "1992-01-10",
                                          "phoneNumber": "19999999999",
                                          "monthlyIncome": 5000,
                                          "creditScore": 800
                                        }
                                        """)
                )
                .andExpect(status().isConflict());

        verify(customerMapper)
                .toInput(any(CustomerRequest.class));

        verify(createCustomerUseCase)
                .execute(input);
    }

    @Test
    @DisplayName("Should return conflict when email already exists")
    void shouldReturnConflictWhenEmailAlreadyExists()
            throws Exception {

        CreateCustomerInput input =
                new CreateCustomerInput(
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800
                );

        when(customerMapper.toInput(any(CustomerRequest.class)))
                .thenReturn(input);

        when(createCustomerUseCase.execute(input))
                .thenThrow(new EmailAlreadyExistsException());

        mockMvc.perform(
                        post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Testando",
                                          "cpf": "292.462.720-64",
                                          "email": "testando@email.com",
                                          "dateOfBirth": "1992-01-10",
                                          "phoneNumber": "19999999999",
                                          "monthlyIncome": 5000,
                                          "creditScore": 800
                                        }
                                        """)
                )
                .andExpect(status().isConflict());

        verify(customerMapper)
                .toInput(any(CustomerRequest.class));

        verify(createCustomerUseCase)
                .execute(input);
    }

    private CustomerOutput customerOutput(UUID id) {

        Instant now = Instant.now();

        return new CustomerOutput(
                id,
                TestConstants.CUSTOMER_NAME,
                "***.***.***-64",
                "testando@email.com",
                TestConstants.CUSTOMER_BIRTH_DATE,
                "19999999999",
                TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                800,
                CustomerStatus.ACTIVE,
                now,
                now
        );
    }

    private CustomerResponse customerResponse(CustomerOutput output) {

        return new CustomerResponse(
                output.id(),
                output.name(),
                output.cpf(),
                output.email(),
                output.dateOfBirth(),
                output.phoneNumber(),
                output.monthlyIncome(),
                output.creditScore(),
                output.status(),
                output.createdAt(),
                output.updatedAt()
        );
    }
}
