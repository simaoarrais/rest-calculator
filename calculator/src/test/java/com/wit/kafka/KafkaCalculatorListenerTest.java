package com.wit.kafka;

import com.wit.model.CalculationRequest;
import com.wit.model.CalculationResponse;
import com.wit.service.CalculatorService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.MDC;
import org.springframework.messaging.MessageHeaders;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class KafkaCalculatorListenerTest {

    private CalculatorService calculatorService;
    private KafkaCalculatorProducer kafkaProducer;
    private KafkaCalculatorListener listener;

    @BeforeEach
    void setup() {
        calculatorService = mock(CalculatorService.class);
        kafkaProducer = mock(KafkaCalculatorProducer.class);
        listener = new KafkaCalculatorListener(calculatorService, kafkaProducer);
        MDC.put("requestId", "test-id");
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void testSumOperation() {
        CalculationRequest request = new CalculationRequest("sum", new BigDecimal("2"), new BigDecimal("3"));
        when(calculatorService.add(new BigDecimal("2"), new BigDecimal("3"))).thenReturn(new BigDecimal("5"));

        listener.listen(request, new MessageHeaders(Map.of()));

        // Need to tell Mockito to capture the response sent by value
        ArgumentCaptor<CalculationResponse> captor = ArgumentCaptor.forClass(CalculationResponse.class);
        verify(kafkaProducer).sendResponse(captor.capture());
        assertEquals(new BigDecimal("5"), captor.getValue().getResult());
    }

    @Test
    void testSubtractionOperation() {
        CalculationRequest request = new CalculationRequest("subtraction", new BigDecimal("5"), new BigDecimal("3"));
        when(calculatorService.subtract(new BigDecimal("5"), new BigDecimal("3"))).thenReturn(new BigDecimal("2"));

        listener.listen(request, new MessageHeaders(Map.of()));

        ArgumentCaptor<CalculationResponse> captor = ArgumentCaptor.forClass(CalculationResponse.class);
        verify(kafkaProducer).sendResponse(captor.capture());
        assertEquals(new BigDecimal("2"), captor.getValue().getResult());
    }

    @Test
    void testMultiplicationOperation() {
        CalculationRequest request = new CalculationRequest("multiplication", new BigDecimal("4"), new BigDecimal("5"));
        when(calculatorService.multiply(new BigDecimal("4"), new BigDecimal("5"))).thenReturn(new BigDecimal("20"));

        listener.listen(request, new MessageHeaders(Map.of()));

        ArgumentCaptor<CalculationResponse> captor = ArgumentCaptor.forClass(CalculationResponse.class);
        verify(kafkaProducer).sendResponse(captor.capture());
        assertEquals(new BigDecimal("20"), captor.getValue().getResult());
    }

    @Test
    void testDivisionOperation() {
        CalculationRequest request = new CalculationRequest("division", new BigDecimal("5"), new BigDecimal("5"));
        when(calculatorService.divide(new BigDecimal("5"), new BigDecimal("5"))).thenReturn(new BigDecimal("1"));

        listener.listen(request, new MessageHeaders(Map.of()));

        ArgumentCaptor<CalculationResponse> captor = ArgumentCaptor.forClass(CalculationResponse.class);
        verify(kafkaProducer).sendResponse(captor.capture());
        assertEquals(new BigDecimal("1"), captor.getValue().getResult());
    }

    @Test
    void testDivisionByZero() {
        CalculationRequest request = new CalculationRequest("division", new BigDecimal("1"), BigDecimal.ZERO);
        when(calculatorService.divide(any(), eq(BigDecimal.ZERO)))
                .thenThrow(new ArithmeticException("Division by zero"));

        listener.listen(request, new MessageHeaders(Map.of()));

        verify(calculatorService).divide(any(), eq(BigDecimal.ZERO));
        verifyNoInteractions(kafkaProducer);
    }

    @Test
    void testUnsupportedOperation() {
        CalculationRequest request = new CalculationRequest("modulo", new BigDecimal("2"), new BigDecimal("3"));

        listener.listen(request, new MessageHeaders(Map.of()));

        verifyNoInteractions(kafkaProducer);
    }

    @Test
    void testHandleExceptionDuringProcessing() {
        // Simulates an exception during processing 
        CalculationRequest request = new CalculationRequest("sum", new BigDecimal("2"), new BigDecimal("3"));
        when(calculatorService.add(any(), any())).thenThrow(new RuntimeException("Test exception"));

        listener.listen(request, new MessageHeaders(Map.of()));

        verify(calculatorService).add(new BigDecimal("2"), new BigDecimal("3"));
        verifyNoInteractions(kafkaProducer);
    }
}