package com.wit.kafka;

import com.wit.model.CalculationRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KafkaRestProducerTest {

    private KafkaTemplate<String, CalculationRequest> kafkaTemplate;
    private KafkaRestProducer producer;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new KafkaRestProducer(kafkaTemplate);
    }
    
    @Test
    void testSendRequest() {
        CalculationRequest request = new CalculationRequest("sum", new BigDecimal("10"), new BigDecimal("20"));

        producer.send(request);

        // Capture the full ProducerRecord
        ArgumentCaptor<ProducerRecord<String, CalculationRequest>> captor = ArgumentCaptor.forClass((Class) ProducerRecord.class);

        verify(kafkaTemplate).send(captor.capture());

        ProducerRecord<String, CalculationRequest> record = captor.getValue();

        assertEquals("operations", record.topic());
        assertEquals("sum", record.value().getOperation());
        assertEquals(new BigDecimal("10"), record.value().getA());
        assertEquals(new BigDecimal("20"), record.value().getB());
    }
}
