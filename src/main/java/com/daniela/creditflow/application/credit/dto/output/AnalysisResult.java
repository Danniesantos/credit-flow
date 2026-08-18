package com.daniela.creditflow.application.credit.dto.output;

public record AnalysisResult(boolean approved,
                             String reason) {


    public static AnalysisResult success() {
        return new AnalysisResult(
                true,
                null);
    }

    public static AnalysisResult failure(String reason) {
        return new AnalysisResult(
                false,
                reason);
    }

    public AnalysisResult {

        if (approved && reason != null) {
            throw new IllegalArgumentException(
                    "Approved analysis cannot contain a reason");
        }

        if (!approved && (reason == null || reason.isBlank())) {
            throw new IllegalArgumentException(
                    "Rejected analysis must contain a reason");
        }
    }
}
