package com.wit.kafka;

import com.wit.model.CalculationResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class KafkaRestListener {

    private final BlockingQueue<CalculationResponse> responseQueue = new ArrayBlockingQueue<>(1);

    @KafkaListener(topics = "calculator-responses", groupId = "rest")
    public void listen(CalculationResponse response) {
        responseQueue.offer(response);
    }

    public CalculationResponse waitForResponse() {
        try {
            return responseQueue.take(); // blocks until a response is available
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for Kafka response", e);
        }
    }
}
