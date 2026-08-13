package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PhoneNumberTest {

    @Test
    @DisplayName("Should create valid phone number")
    void shouldCreateValidPhoneNumber() {

        PhoneNumber phone =
                new PhoneNumber("19999999999");

        assertThat(phone.value())
                .isEqualTo("19999999999");
    }

    @Test
    @DisplayName("Should normalize phone number")
    void shouldNormalizePhoneNumber() {

        PhoneNumber phone =
                new PhoneNumber("(19) 99999-9999");

        assertThat(phone.value())
                .isEqualTo("19999999999");
    }

    @Test
    @DisplayName("Should reject null phone number")
    void shouldRejectNullPhoneNumber() {

        assertThatThrownBy(() ->
                new PhoneNumber(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Phone number cannot be null");
    }

    @Test
    @DisplayName("Should reject phone number with less than 10 digits")
    void shouldRejectPhoneNumberWithLessThan10Digits() {

        assertThatThrownBy(() ->
                new PhoneNumber("123456789")
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Invalid phone number");
    }

    @Test
    @DisplayName("Should reject phone number with more than 11 digits")
    void shouldRejectPhoneNumberWithMoreThan11Digits() {

        assertThatThrownBy(() ->
                new PhoneNumber("123456789012")
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Invalid phone number");
    }
}