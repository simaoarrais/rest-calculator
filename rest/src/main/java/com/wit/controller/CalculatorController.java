package com.wit.controller;

import com.wit.kafka.KafkaRestProducer;
import com.wit.kafka.KafkaRestListener;
import com.wit.model.CalculationRequest;
import com.wit.model.CalculationResponse;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;




@RestController
public class CalculatorController {
    
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    private final KafkaRestProducer producer;
    private final KafkaRestListener responseListener;

    public CalculatorController(KafkaRestProducer producer, KafkaRestListener responseListener) {
        this.producer = producer;
        this.responseListener = responseListener;
    }

    @GetMapping("/sum")
    public Map<String, BigDecimal> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        logger.info("Received /sum request with a={} and b={}", a, b);
        return process("sum", a, b);
    }

    @GetMapping("/subtraction")
    public Map<String, BigDecimal> subtract(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        logger.info("Received /subtraction request with a={} and b={}", a, b);
        return process("subtraction", a, b);
    }

    @GetMapping("/multiplication")
    public Map<String, BigDecimal> multiply(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        logger.info("Received /multiplication request with a={} and b={}", a, b);
        return process("multiplication", a, b);
    }

    @GetMapping("/division")
    public Map<String, BigDecimal> divide(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        logger.info("Received /division request with a={} and b={}", a, b);

        if (b.compareTo(BigDecimal.ZERO) == 0) {
            logger.warn("Attempted division by zero: a={}, b={}", a, b);
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return process("division", a, b);
    }

    private Map<String, BigDecimal> process(String operation, BigDecimal a, BigDecimal b) {
        CalculationRequest request = new CalculationRequest(operation, a, b);
        logger.debug("Sending CalculationRequest to Kafka: {}", request);
        producer.send(request);

        logger.debug("Waiting for CalculationResponse...");
        CalculationResponse response = responseListener.waitForResponse();

        if (response == null) {
            logger.error("No response received from calculator module for operation: {}", operation);
            throw new IllegalStateException("No response from calculator module");
        }
        
        logger.info("Received CalculationResponse: result={}", response.getResult());
        return Map.of("result", response.getResult());
    }
}
