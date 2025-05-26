package com.wit.kafka;

import com.wit.model.CalculationResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class KafkaRestListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaRestListener.class);

    private final BlockingQueue<CalculationResponse> responseQueue = new ArrayBlockingQueue<>(1);

    @KafkaListener(topics = "calculator-responses", groupId = "rest")
    public void listen(CalculationResponse response) {
        responseQueue.offer(response);
    }

    public CalculationResponse waitForResponse() {
        try {
            CalculationResponse response = responseQueue.take();
            logger.debug("Returning CalculationResponse to REST layer: {}", response);
            return response; // blocks until a response is available
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for response from Kafka", e);
            throw new RuntimeException("Interrupted while waiting for Kafka response", e);
        }
    }
}
