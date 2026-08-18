package com.daniela.creditflow.domain.valueObject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CPFTest {

    @Test
    @DisplayName("Should create valid CPF")
    void shouldCreateValidCpf() {

        CPF cpf =
                new CPF("292.462.720-64");

        assertThat(cpf.value())
                .isEqualTo("29246272064");
    }

    @Test
    @DisplayName("Should normalize CPF with formatting")
    void shouldNormalizeCpf() {

        CPF cpf =
                new CPF("292.462.720-64");

        assertThat(cpf.value())
                .isEqualTo("29246272064");
    }

    @Test
    @DisplayName("Should reject null CPF")
    void shouldRejectNullCpf() {

        assertThatThrownBy(() ->
                new CPF(null)
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("CPF cannot be null");
    }

    @Test
    @DisplayName("Should reject CPF with invalid number of digits")
    void shouldRejectCpfWithInvalidNumberOfDigits() {

        assertThatThrownBy(() ->
                new CPF("123456789")
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("CPF must have 11 digits");
    }

    @Test
    @DisplayName("Should reject CPF with repeated digits")
    void shouldRejectCpfWithRepeatedDigits() {

        assertThatThrownBy(() ->
                new CPF("11111111111")
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Invalid CPF format");
    }

    @Test
    @DisplayName("Should reject CPF with invalid verification digits")
    void shouldRejectCpfWithInvalidVerificationDigits() {

        assertThatThrownBy(() ->
                new CPF("29246272065")
        )
                .isInstanceOf(InvalidDomainStateException.class)
                .hasMessage("Invalid CPF format");
    }
}