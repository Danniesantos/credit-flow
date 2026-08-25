package com.daniela.creditflow.domain.valueobject;

import com.daniela.creditflow.domain.exceptions.InvalidDomainStateException;

public record CPF(String value) {

    public CPF {

        if (value == null) {
            throw new InvalidDomainStateException(
                    "CPF cannot be null"
            );
        }

        String normalizedValue = normalize(value);

        validate(normalizedValue);

        value = normalizedValue;
    }

    private static String normalize(String value) {
        return value.replaceAll("\\D", "");
    }

    private static void validate(String value) {

        if (value.length() != 11) {
            throw new InvalidDomainStateException("CPF must have 11 digits");
        }

        if (value.matches("(\\d)\\1{10}")) {
            throw new InvalidDomainStateException("Invalid CPF format");
        }

        if (!isValid(value)) {
            throw new InvalidDomainStateException("Invalid CPF format");
        }
    }

    private static boolean isValid(String cpf) {

        String base = cpf.substring(0, 9);

        int firstDigit = calculateDigit(base, 10);

        String cpfWithFirstDigit = base + firstDigit;

        int secondDigit = calculateDigit(cpfWithFirstDigit, 11);

        String calculatedCpf =
                cpfWithFirstDigit + secondDigit;

        return cpf.equals(calculatedCpf);
    }

    private static int calculateDigit(String cpf, int weight) {
        int sum = 0;

        for (int i = 0; i < cpf.length(); i++) {
            char digit = cpf.charAt(i);
            int number = digit - '0';
            sum += number * weight;
            weight--;
        }

        int remainder = sum % 11;

        if (remainder < 2) {
            return 0;
        }

        return 11 - remainder;
    }

}
