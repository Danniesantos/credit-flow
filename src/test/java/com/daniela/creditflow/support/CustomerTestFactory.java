package com.daniela.creditflow.support;

import com.daniela.creditflow.domain.model.Customer;
import com.daniela.creditflow.domain.model.CustomerData;
import com.daniela.creditflow.domain.valueobject.CPF;
import com.daniela.creditflow.domain.valueobject.CreditScore;
import com.daniela.creditflow.domain.valueobject.Email;
import com.daniela.creditflow.domain.valueobject.PhoneNumber;

public final class CustomerTestFactory {

    private CustomerTestFactory() {
    }

    public static Customer customer() {

        return new Customer(
                customerData(),
                TestConstants.FIXED_CLOCK
        );
    }

    public static CustomerData customerData() {

        return new CustomerData(
                TestConstants.CUSTOMER_NAME,
                new CPF("292.462.720-64"),
                new Email("testando@email.com"),
                TestConstants.CUSTOMER_BIRTH_DATE,
                new PhoneNumber("19999999999"),
                new CreditScore(800),
                TestConstants.CUSTOMER_MONTHLY_INCOME
        );
    }

    public static Customer inactiveCustomer() {

        Customer customer = customer();

        customer.deactivate();

        return customer;
    }

    public static Customer customerWithBadScore() {

        Customer customer = customer();

        customer.update(
                new CustomerData(
                        customer.getName(),
                        customer.getCpf(),
                        customer.getEmail(),
                        customer.getDateOfBirth(),
                        customer.getPhoneNumber(),
                        new CreditScore(300),
                        customer.getMonthlyIncome()
                )
        );

        return customer;
    }
}
