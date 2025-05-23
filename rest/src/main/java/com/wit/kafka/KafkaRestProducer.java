package com.wit.kafka;

import com.wit.model.CalculationRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaRestProducer {

    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    public KafkaRestProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CalculationRequest request) {
        kafkaTemplate.send("operations", request);
    }
}
