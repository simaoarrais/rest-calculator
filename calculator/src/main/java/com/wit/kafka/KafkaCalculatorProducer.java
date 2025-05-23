package com.wit.kafka;

import com.wit.model.CalculationResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaCalculatorProducer {

    private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

    public KafkaCalculatorProducer(KafkaTemplate<String, CalculationResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendResponse(CalculationResponse response) {
        kafkaTemplate.send("calculator-responses", response);
    }
}
