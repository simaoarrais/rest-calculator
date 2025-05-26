package com.wit.kafka;

import com.wit.model.CalculationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;

@Service
public class KafkaRestProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaRestProducer.class);

    private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

    public KafkaRestProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(CalculationRequest request) {
        String requestId = MDC.get("requestId");
        ProducerRecord<String, CalculationRequest> record = new ProducerRecord<>("operations", request);

        if (requestId != null) {
            record.headers().add(new RecordHeader("requestId", requestId.getBytes(StandardCharsets.UTF_8)));
        }

        logger.debug("Sent Kafka request with ID: {}", requestId);
        kafkaTemplate.send(record);
    }
}
