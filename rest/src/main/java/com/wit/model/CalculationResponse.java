package com.wit.model;

import java.math.BigDecimal;

public class CalculationResponse {
    private BigDecimal result;

    public CalculationResponse() {}

    public CalculationResponse(BigDecimal result) {
        this.result = result;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    // For better logging and debugging
    @Override
    public String toString() {
        return "CalculationResponse{" +
                "result=" + result +
                '}';
    }
}
