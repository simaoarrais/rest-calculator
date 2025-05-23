package com.wit.controller;

import com.wit.kafka.KafkaRestProducer;
import com.wit.kafka.KafkaRestListener;
import com.wit.model.CalculationRequest;
import com.wit.model.CalculationResponse;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, Double> sum(@RequestParam double a, @RequestParam double b) {
        CalculationRequest request = new CalculationRequest("sum", a, b);
        producer.send(request);

        CalculationResponse response = responseListener.waitForResponse();
        return Map.of("result", response.getResult());
    }
}
