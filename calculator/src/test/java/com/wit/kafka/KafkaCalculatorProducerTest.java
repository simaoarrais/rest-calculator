package com.wit.kafka;

import com.wit.model.CalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KafkaCalculatorProducerTest {

    private KafkaTemplate<String, CalculationResponse> kafkaTemplate;
    private KafkaCalculatorProducer producer;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new KafkaCalculatorProducer(kafkaTemplate);
    }

    @Test
    void testSendResponse() {
        CalculationResponse response = new CalculationResponse(new BigDecimal("42"));

        producer.sendResponse(response);

        // Capture topic and payload
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<CalculationResponse> payloadCaptor = ArgumentCaptor.forClass(CalculationResponse.class);

        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), payloadCaptor.capture());

        assertEquals("calculator-responses", topicCaptor.getValue());
        assertEquals(new BigDecimal("42"), payloadCaptor.getValue().getResult());
    }
}