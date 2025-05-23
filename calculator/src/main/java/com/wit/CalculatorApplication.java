package com.wit;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class CalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}

	@Bean
    public NewTopic topic() {
        return TopicBuilder.name("operations")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(id = "calculatorListener", topics = "operations")
    public void listen(String in) {
        System.out.println(in);
    }
}