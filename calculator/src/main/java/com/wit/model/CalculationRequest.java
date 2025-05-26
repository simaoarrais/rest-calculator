package com.wit.model;

import java.math.BigDecimal;

public class CalculationRequest {
    private String operation;
    private BigDecimal a;
    private BigDecimal b;

    public CalculationRequest() {} // default constructor

    public CalculationRequest(String operation, BigDecimal a, BigDecimal b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
    }

    // Getters and setters
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public BigDecimal getA() { return a; }
    public void setA(BigDecimal a) { this.a = a; }

    public BigDecimal getB() { return b; }
    public void setB(BigDecimal b) { this.b = b; }

    // For better logging and debugging
    @Override
    public String toString() {
        return "CalculationRequest{" +
                "operation='" + operation + '\'' +
                ", a=" + a +
                ", b=" + b +
                '}';
    }
}
