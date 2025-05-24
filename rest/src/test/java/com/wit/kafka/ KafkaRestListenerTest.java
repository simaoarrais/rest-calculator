package com.wit.kafka;

import com.wit.model.CalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class KafkaRestListenerTest {

    private KafkaRestListener listener;

    @BeforeEach
    void setUp() {
        listener = new KafkaRestListener();
    }

    @Test
    void testListenAndWaitForResponse() {
        CalculationResponse expectedResponse = new CalculationResponse(new BigDecimal("42"));

        listener.listen(expectedResponse);
        CalculationResponse result = listener.waitForResponse();

        assertNotNull(result);
        assertEquals(new BigDecimal("42"), result.getResult());
    }
}
