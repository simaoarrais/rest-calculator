package com.wit.kafka;

import com.wit.model.CalculationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaRestProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaRestProducer.class);

    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    public KafkaRestProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CalculationRequest request) {
        logger.debug("Sending CalculationRequest to topic 'operations': {}", request);
        kafkaTemplate.send("operations", request);
    }
}
