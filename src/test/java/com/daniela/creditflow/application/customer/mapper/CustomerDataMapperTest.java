package com.daniela.creditflow.application.customer.mapper;

import com.daniela.creditflow.application.customer.dto.input.CreateCustomerInput;
import com.daniela.creditflow.application.customer.dto.input.UpdateCustomerInput;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.support.CustomerTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerDataMapperTest {

    private final CustomerDataMapper mapper =
            new CustomerDataMapper();

    @Test
    @DisplayName("Should map create customer input to customer data")
    void shouldMapCreateCustomerInputToCustomerData() {

        CustomerData expected =
                CustomerTestFactory.customerData();

        CreateCustomerInput input =
                new CreateCustomerInput(
                        expected.name(),
                        expected.cpf().value(),
                        expected.email().value(),
                        expected.dateOfBirth(),
                        expected.phoneNumber().value(),
                        expected.monthlyIncome().value(),
                        expected.creditScore().value()
                );

        CustomerData result =
                mapper.from(input);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Should map update customer input to customer data")
    void shouldMapUpdateCustomerInputToCustomerData() {

        CustomerData expected =
                CustomerTestFactory.customerData();

        UpdateCustomerInput input =
                new UpdateCustomerInput(
                        UUID.randomUUID(),
                        expected.name(),
                        expected.cpf().value(),
                        expected.email().value(),
                        expected.dateOfBirth(),
                        expected.phoneNumber().value(),
                        expected.monthlyIncome().value(),
                        expected.creditScore().value()
                );

        CustomerData result =
                mapper.from(input);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
