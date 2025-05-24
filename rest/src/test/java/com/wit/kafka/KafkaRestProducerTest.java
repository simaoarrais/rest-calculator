package com.wit.kafka;

import com.wit.model.CalculationRequest;
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

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new KafkaRestProducer(kafkaTemplate);
    }

    @Test
    void testSendRequest() {
        CalculationRequest request = new CalculationRequest("sum", new BigDecimal("10"), new BigDecimal("20"));

        producer.send(request);

        ArgumentCaptor<CalculationRequest> captor = ArgumentCaptor.forClass(CalculationRequest.class);
        verify(kafkaTemplate).send(eq("operations"), captor.capture());

        CalculationRequest captured = captor.getValue();
        assertEquals("sum", captured.getOperation());
        assertEquals(new BigDecimal("10"), captured.getA());
        assertEquals(new BigDecimal("20"), captured.getB());
    }
}
