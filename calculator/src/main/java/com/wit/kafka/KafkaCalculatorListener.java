package com.wit.kafka;

import com.wit.model.CalculationRequest;
import com.wit.model.CalculationResponse;
import com.wit.service.CalculatorService;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaCalculatorListener {

    private final CalculatorService calculatorService;
    private final KafkaCalculatorProducer kafkaSender;

    public KafkaCalculatorListener(CalculatorService calculatorService, KafkaCalculatorProducer kafkaSender) {
        this.calculatorService = calculatorService;
        this.kafkaSender = kafkaSender;
    }

    @KafkaListener(topics = "operations", groupId = "calculator")
    public void listen(CalculationRequest request) {
        BigDecimal result;
        switch (request.getOperation()) {
            case "sum" -> result = calculatorService.add(request.getA(), request.getB());
            case "subtraction" -> result = calculatorService.subtract(request.getA(), request.getB());
            case "multiplication" -> result = calculatorService.multiply(request.getA(), request.getB());
            case "division" -> result = calculatorService.divide(request.getA(), request.getB());
            default -> throw new IllegalArgumentException("Unsupported operation: " + request.getOperation());
        }

        kafkaSender.sendResponse(new CalculationResponse(result));
    }
}
