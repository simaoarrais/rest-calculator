package com.wit.controller;

import com.wit.kafka.KafkaRestListener;
import com.wit.kafka.KafkaRestProducer;
import com.wit.model.CalculationResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KafkaRestProducer producer;

    @MockitoBean
    private KafkaRestListener listener;

    @Test
    void testSumEndpoint() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(new CalculationResponse(new BigDecimal("5")));

        mockMvc.perform(get("/sum")
                .param("a", "2")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("5")); // $ refers to the root of the JSON
    }

    @Test
    void testSubtractEndpoint() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(new CalculationResponse(new BigDecimal("1")));

        mockMvc.perform(get("/subtraction")
                .param("a", "3")
                .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("1"));
    }

    @Test
    void testMultiplyEndpoint() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(new CalculationResponse(new BigDecimal("6")));

        mockMvc.perform(get("/multiplication")
                .param("a", "2")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("6"));
    }

    @Test
    void testDivideEndpoint() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(new CalculationResponse(new BigDecimal("3")));

        mockMvc.perform(get("/division")
                .param("a", "3")
                .param("b", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("3"));
    }

    @Test
    void testDivisionByZero() throws Exception {
        mockMvc.perform(get("/division")
                .param("a", "10")
                .param("b", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingParameter() throws Exception {
        mockMvc.perform(get("/sum").param("a", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidParameter() throws Exception {
        mockMvc.perform(get("/sum")
                .param("a", "abc")
                .param("b", "3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNullParameter() throws Exception {
        mockMvc.perform(get("/sum")
                .param("a", "5")
                .param("b", (String) null))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLargeNumbers() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(new CalculationResponse(new BigDecimal("11111111111111111111111111111110")));

        mockMvc.perform(get("/sum")
                .param("a", "5555555555555555555555555555555")
                .param("b", "5555555555555555555555555555555"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("11111111111111111111111111111110"));
    }

    @Test
    void testNegativeNumbers() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(new CalculationResponse(new BigDecimal("-1")));

        mockMvc.perform(get("/subtraction")
                .param("a", "2")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("-1"));
    }

    @Test
    void testDivisionPrecision() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(new CalculationResponse(new BigDecimal("0.3333333333333333")));

        mockMvc.perform(get("/division")
                .param("a", "1")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("0.3333333333333333"));
    }

    @Test
    void testUnsupportedOperation() throws Exception {
        mockMvc.perform(get("/unsupported"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testKafkaListenerFailure() throws Exception {
        Mockito.when(listener.waitForResponse()).thenThrow(new RuntimeException("Kafka error"));

        mockMvc.perform(get("/sum")
                .param("a", "2")
                .param("b", "3"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testEmptyKafkaResponse() throws Exception {
        Mockito.when(listener.waitForResponse()).thenReturn(null);

        mockMvc.perform(get("/sum")
                .param("a", "2")
                .param("b", "3"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testNoHandlerFound() throws Exception {
        mockMvc.perform(get("/nonexistent"))
                .andExpect(status().isNotFound());
    }

}
