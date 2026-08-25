package com.daniela.creditflow.infrastructure.web.mapper;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.domain.model.CustomerStatus;
import com.daniela.creditflow.domain.valueobject.CustomerId;
import com.daniela.creditflow.infrastructure.web.request.CustomerRequest;
import com.daniela.creditflow.infrastructure.web.response.CustomerResponse;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerWebMapperTest {

    private final CustomerWebMapper mapper =
            new CustomerWebMapper();

    @Test
    @DisplayName("Should convert customer request to create input")
    void shouldConvertCustomerRequestToCreateInput() {

        CustomerRequest request =
                new CustomerRequest(
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800
                );

        CreateCustomerInput result =
                mapper.toInput(request);

        assertThat(result.name())
                .isEqualTo(request.getName());

        assertThat(result.cpf())
                .isEqualTo(request.getCpf());

        assertThat(result.email())
                .isEqualTo(request.getEmail());

        assertThat(result.dateOfBirth())
                .isEqualTo(request.getDateOfBirth());

        assertThat(result.phoneNumber())
                .isEqualTo(request.getPhoneNumber());

        assertThat(result.monthlyIncome())
                .isEqualByComparingTo(request.getMonthlyIncome());

        assertThat(result.creditScore())
                .isEqualTo(request.getCreditScore());
    }

    @Test
    @DisplayName("Should convert customer output to response")
    void shouldConvertCustomerOutputToResponse() {

        UUID id = UUID.randomUUID();
        LocalDate birthDate = LocalDate.of(1995, 5, 10);
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        CustomerOutput output =
                new CustomerOutput(
                        id,
                        "Daniela",
                        "***.***.***-64",
                        "testando@email.com",
                        birthDate,
                        "19999999999",
                        new BigDecimal("5000.00"),
                        800,
                        CustomerStatus.ACTIVE,
                        createdAt,
                        updatedAt
                );

        CustomerResponse result =
                mapper.toResponse(output);

        assertThat(result.id())
                .isEqualTo(id);

        assertThat(result.name())
                .isEqualTo(output.name());

        assertThat(result.cpf())
                .isEqualTo(output.cpf());

        assertThat(result.email())
                .isEqualTo(output.email());

        assertThat(result.dateOfBirth())
                .isEqualTo(output.dateOfBirth());

        assertThat(result.phoneNumber())
                .isEqualTo(output.phoneNumber());

        assertThat(result.monthlyIncome())
                .isEqualByComparingTo(output.monthlyIncome());

        assertThat(result.creditScore())
                .isEqualTo(output.creditScore());

        assertThat(result.status())
                .isEqualTo(output.status());

        assertThat(result.createdAt())
                .isEqualTo(output.createdAt());

        assertThat(result.updatedAt())
                .isEqualTo(output.updatedAt());
    }

    @Test
    @DisplayName("Should convert customer request to update input")
    void shouldConvertCustomerRequestToUpdateInput() {

        UUID id = UUID.randomUUID();

        CustomerRequest request =
                new CustomerRequest(
                        TestConstants.CUSTOMER_NAME,
                        "292.462.720-64",
                        "testando@email.com",
                        TestConstants.CUSTOMER_BIRTH_DATE,
                        "19999999999",
                        TestConstants.CUSTOMER_MONTHLY_INCOME.value(),
                        800
                );

        UpdateCustomerInput result =
                mapper.toUpdateInput(id, request);

        assertThat(result.id())
                .isEqualTo(id);

        assertThat(result.name())
                .isEqualTo(request.getName());

        assertThat(result.cpf())
                .isEqualTo(request.getCpf());

        assertThat(result.email())
                .isEqualTo(request.getEmail());

        assertThat(result.dateOfBirth())
                .isEqualTo(request.getDateOfBirth());

        assertThat(result.phoneNumber())
                .isEqualTo(request.getPhoneNumber());

        assertThat(result.monthlyIncome())
                .isEqualByComparingTo(request.getMonthlyIncome());

        assertThat(result.creditScore())
                .isEqualTo(request.getCreditScore());
    }

    @Test
    @DisplayName("Should convert UUID to CustomerId")
    void shouldConvertUuidToCustomerId() {

        UUID id = UUID.randomUUID();

        CustomerId result =
                mapper.toCustomerId(id);

        assertThat(result.value())
                .isEqualTo(id);
    }
}