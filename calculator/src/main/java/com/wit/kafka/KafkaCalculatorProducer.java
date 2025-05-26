package com.wit.kafka;

import com.wit.model.CalculationResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaCalculatorProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCalculatorProducer.class);

    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    public KafkaCalculatorProducer(KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendResponse(CalculationResponse response) {
        logger.debug("Sending response to Kafka: {}", response);
        kafkaTemplate.send("calculator-responses", response);
    }
}
