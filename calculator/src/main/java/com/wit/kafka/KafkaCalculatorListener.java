package com.wit.kafka;

import com.wit.model.CalculationRequest;
import com.wit.model.CalculationResponse;
import com.wit.service.CalculatorService;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaCalculatorListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCalculatorListener.class);

    private final CalculatorService calculatorService;
    private final KafkaCalculatorProducer kafkaSender;

    public KafkaCalculatorListener(CalculatorService calculatorService, KafkaCalculatorProducer kafkaSender) {
        this.calculatorService = calculatorService;
        this.kafkaSender = kafkaSender;
    }

    @KafkaListener(topics = "operations", groupId = "calculator")
    public void listen(@Payload CalculationRequest request, @Headers MessageHeaders headers)  {

        String requestId = null;
        if (headers.containsKey("requestId")) {
            requestId = new String((byte[]) headers.get("requestId"));
            MDC.put("requestId", requestId);
        }
        
        try {
            logger.info("Received calculation request: {}", request);
            logger.debug("Request ID from headers: {}", requestId);

            BigDecimal result = switch (request.getOperation()) {
                case "sum" -> result = calculatorService.add(request.getA(), request.getB());
                case "subtraction" -> result = calculatorService.subtract(request.getA(), request.getB());
                case "multiplication" -> result = calculatorService.multiply(request.getA(), request.getB());
                case "division" -> result = calculatorService.divide(request.getA(), request.getB());
                default -> throw new IllegalArgumentException("Unsupported operation: " + request.getOperation());
            };
            
            kafkaSender.sendResponse(new CalculationResponse(result));
            logger.info("Calculated result: {}", result);
            
        } catch (Exception e) {
            logger.error("Error processing calculation request {}: {}", request, e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }
}
