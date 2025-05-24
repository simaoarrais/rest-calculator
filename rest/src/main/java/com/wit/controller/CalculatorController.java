package com.wit.controller;

import com.wit.kafka.KafkaRestProducer;
import com.wit.kafka.KafkaRestListener;
import com.wit.model.CalculationRequest;
import com.wit.model.CalculationResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class CalculatorController {

    private final KafkaRestProducer producer;
    private final KafkaRestListener responseListener;

    public CalculatorController(KafkaRestProducer producer, KafkaRestListener responseListener) {
        this.producer = producer;
        this.responseListener = responseListener;
    }

    @GetMapping("/sum")
    public Map<String, BigDecimal> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return process("sum", a, b);
    }

    @GetMapping("/subtraction")
    public Map<String, BigDecimal> subtract(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return process("subtraction", a, b);
    }

    @GetMapping("/multiplication")
    public Map<String, BigDecimal> multiply(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return process("multiplication", a, b);
    }

    @GetMapping("/division")
    public Map<String, BigDecimal> divide(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return process("division", a, b);
    }

    private Map<String, BigDecimal> process(String operation, BigDecimal a, BigDecimal b) {
        CalculationRequest request = new CalculationRequest(operation, a, b);
        producer.send(request);
        CalculationResponse response = responseListener.waitForResponse();

        if (response == null) {
            throw new IllegalStateException("No response from calculator module");
        }
    
        return Map.of("result", response.getResult());
    }
}
