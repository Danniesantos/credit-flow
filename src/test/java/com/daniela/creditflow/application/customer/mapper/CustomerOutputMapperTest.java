package com.daniela.creditflow.application.customer.mapper;

import com.daniela.creditflow.application.customer.dto.output.CustomerOutput;
import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerOutputMapperTest {

    private final CustomerOutputMapper mapper =
            new CustomerOutputMapper();

    @Test
    @DisplayName("Should map customer to output")
    void shouldMapCustomerToOutput() {

        Customer customer =
                CustomerTestFactory.customer();

        CustomerOutput output =
                mapper.from(customer);

        assertThat(output.id())
                .isEqualTo(customer.getId().value());

        assertThat(output.name())
                .isEqualTo(customer.getName());

        assertThat(output.cpf())
                .isEqualTo("***.***.***-64");

        assertThat(output.email())
                .isEqualTo(customer.getEmail().value());

        assertThat(output.dateOfBirth())
                .isEqualTo(customer.getDateOfBirth());

        assertThat(output.phoneNumber())
                .isEqualTo(customer.getPhoneNumber().value());

        assertThat(output.monthlyIncome())
                .isEqualByComparingTo(
                        customer.getMonthlyIncome().value()
                );

        assertThat(output.creditScore())
                .isEqualTo(customer.getCreditScore().value());

        assertThat(output.status())
                .isEqualTo(customer.getStatus());

        assertThat(output.createdAt())
                .isEqualTo(customer.getCreatedAt());

        assertThat(output.updatedAt())
                .isEqualTo(customer.getUpdatedAt());
    }
}