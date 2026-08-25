package com.daniela.creditflow.domain.model;

import com.daniela.creditflow.domain.exceptions.CustomerAlreadyInactiveException;
import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import com.daniela.creditflow.domain.valueobject.CreditScore;
import com.daniela.creditflow.domain.valueobject.Email;
import com.daniela.creditflow.domain.valueobject.Money;
import com.daniela.creditflow.support.CustomerTestFactory;
import com.daniela.creditflow.support.TestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CustomerTest {

    private static final Clock CLOCK = TestConstants.FIXED_CLOCK;

    @Test
    @DisplayName("Should create active customer")
    void shouldCreateActiveCustomer() {

        Customer customer =
                CustomerTestFactory.customer();

        assertThat(customer.isActive())
                .isTrue();

        assertThat(customer.isInactive())
                .isFalse();
    }

    @Test
    @DisplayName("Should not create customer with blank name")
    void shouldNotCreateCustomerWithBlankName() {

        CustomerData data =
                CustomerTestFactory.customerData();

        CustomerData invalidData =
                new CustomerData(
                        "   ",
                        data.cpf(),
                        data.email(),
                        data.dateOfBirth(),
                        data.phoneNumber(),
                        data.creditScore(),
                        data.monthlyIncome()
                );

        assertThatThrownBy(() ->
                new Customer(invalidData, CLOCK)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Name cannot be blank");
    }

    @Test
    @DisplayName("Should not create customer with name shorter than three characters")
    void shouldNotCreateCustomerWithShortName() {

        CustomerData data =
                CustomerTestFactory.customerData();

        CustomerData invalidData =
                new CustomerData(
                        "AB",
                        data.cpf(),
                        data.email(),
                        data.dateOfBirth(),
                        data.phoneNumber(),
                        data.creditScore(),
                        data.monthlyIncome()
                );

        assertThatThrownBy(() ->
                new Customer(invalidData, CLOCK)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Name must have at least 3 characters");
    }

    @Test
    @DisplayName("Should not create customer with future date of birth")
    void shouldNotCreateCustomerWithFutureDateOfBirth() {

        CustomerData data =
                CustomerTestFactory.customerData();

        CustomerData invalidData =
                new CustomerData(
                        data.name(),
                        data.cpf(),
                        data.email(),
                        LocalDate.now().plusDays(1),
                        data.phoneNumber(),
                        data.creditScore(),
                        data.monthlyIncome()
                );

        assertThatThrownBy(() ->
                new Customer(invalidData, CLOCK)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage(
                        "Date of birth cannot be in the future"
                );
    }

    @Test
    @DisplayName("Should update customer data")
    void shouldUpdateCustomerData() {

        Customer customer =
                CustomerTestFactory.customer();

        Instant previousUpdatedAt =
                customer.getUpdatedAt();

        CustomerData newData =
                new CustomerData(
                        "Maria Santos",
                        customer.getCpf(),
                        new Email("maria@email.com"),
                        customer.getDateOfBirth(),
                        customer.getPhoneNumber(),
                        new CreditScore(750),
                        new Money(new BigDecimal("7000.00"))
                );

        customer.update(newData);

        assertThat(customer.getName())
                .isEqualTo("Maria Santos");

        assertThat(customer.getEmail())
                .isEqualTo(newData.email());

        assertThat(customer.getCreditScore())
                .isEqualTo(newData.creditScore());

        assertThat(customer.getMonthlyIncome())
                .isEqualTo(newData.monthlyIncome());

        assertThat(customer.getUpdatedAt())
                .isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    @DisplayName("Should deactivate active customer")
    void shouldDeactivateActiveCustomer() {

        Customer customer =
                CustomerTestFactory.customer();

        customer.deactivate();

        assertThat(customer.isInactive())
                .isTrue();

        assertThat(customer.isActive())
                .isFalse();
    }

    @Test
    @DisplayName("Should not deactivate an already inactive customer")
    void shouldNotDeactivateAlreadyInactiveCustomer() {

        Customer customer =
                CustomerTestFactory.inactiveCustomer();

        assertThatThrownBy(customer::deactivate)
                .isInstanceOf(CustomerAlreadyInactiveException.class);
    }
}