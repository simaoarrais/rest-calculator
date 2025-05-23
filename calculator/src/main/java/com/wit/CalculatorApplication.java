package com.wit;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class CalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculatorApplication.class, args);
	}

	@Bean
	public NewTopic operationsTopic() {
		return TopicBuilder.name("operations")
				.partitions(10)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic responseTopic() {
		return TopicBuilder.name("calculator-responses")
				.partitions(10)
				.replicas(1)
				.build();
	}
}
