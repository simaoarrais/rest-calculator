package com.wit.controller;

import com.wit.kafka.KafkaProducerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class KafkaTestController {

    private final KafkaProducerService kafkaProducer;

    public KafkaTestController(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/send")
    public String send(@RequestParam String msg) {
        System.out.println(msg);
        kafkaProducer.send(msg);
        return "Message sent: " + msg;
    }
}