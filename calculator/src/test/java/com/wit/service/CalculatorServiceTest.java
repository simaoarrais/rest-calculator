package com.wit.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorServiceTest {

    private final CalculatorService calculatorService = new CalculatorService();

    @Test
    void testAddBasic() {
        BigDecimal result = calculatorService.add(new BigDecimal("2"), new BigDecimal("3"));
        assertEquals(new BigDecimal("5"), result);
    }

    @Test
    void testAddDecimal() {
        BigDecimal result = calculatorService.add(new BigDecimal("1.5"), new BigDecimal("1.5"));
        assertEquals(new BigDecimal("3.0"), result);
    }

    @Test
    void testAddZero() {
        BigDecimal result = calculatorService.add(new BigDecimal("0"), new BigDecimal("0"));
        assertEquals(new BigDecimal("0"), result);
    }

    @Test
    void testAddNegative() {
        BigDecimal result = calculatorService.add(new BigDecimal("-1"), new BigDecimal("-1"));
        assertEquals(new BigDecimal("-2"), result);
    }

    @Test
    void testAddLargeNumbers() {
        // 31 Digits
        BigDecimal a = new BigDecimal("5555555555555555555555555555555");
        BigDecimal b = new BigDecimal("5555555555555555555555555555555");
        BigDecimal expected = new BigDecimal("11111111111111111111111111111110");
        BigDecimal result = calculatorService.add(a, b);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
        ", 1",
        "1, ",
    })
    void testAddNullInputParameterized(BigDecimal a, BigDecimal b) {
        assertThrows(NullPointerException.class, () -> {
            calculatorService.add(a, b);
        });
    }

    @Test
    void testSubtractBasic() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("5"), result);
    }

    @Test
    void testSubtractDecimal() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("5.5"), new BigDecimal("2.2"));
        assertEquals(new BigDecimal("3.3"), result);
    }

    @Test
    void testSubtractZero() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("0"), new BigDecimal("0"));
        assertEquals(new BigDecimal("0"), result);
    }

    @Test
    void testSubtractNegative() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("-5"), new BigDecimal("-10"));
        assertEquals(new BigDecimal("5"), result);
    }

    @Test
    void testSubtractLargeNumbers() {
        // 31 Digits
        BigDecimal a = new BigDecimal("5555555555555555555555555555556");
        BigDecimal b = new BigDecimal("5555555555555555555555555555555");
        BigDecimal expected = new BigDecimal("1");
        BigDecimal result = calculatorService.subtract(a, b);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
        ", 1",
        "1, ",
    })
    void testSubtractNullInputParameterized(BigDecimal a, BigDecimal b) {
        assertThrows(NullPointerException.class, () -> {
            calculatorService.subtract(a, b);
        });
    }

    @Test
    void testMultiplyBasic() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("2"), new BigDecimal("3"));
        assertEquals(new BigDecimal("6"), result);
    }

    @Test
    void testMultiplyDecimal() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("2.5"), new BigDecimal("2.5"));
        assertEquals(new BigDecimal("6.25"), result);
    }

    @Test
    void testMultiplyZero() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("100"), new BigDecimal("0"));
        assertEquals(new BigDecimal("0"), result);
    }

    @Test
    void testMultiplyNegative() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("-5"), new BigDecimal("2"));
        assertEquals(new BigDecimal("-10"), result);
    }

    @Test
    void testMultiplyLargeNumbers() {
        // 31 Digits
        BigDecimal a = new BigDecimal("5555555555555555555555555555555");
        BigDecimal b = new BigDecimal("2");
        BigDecimal expected = new BigDecimal("11111111111111111111111111111110");
        BigDecimal result = calculatorService.multiply(a, b);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
        ", 1",
        "1, ",
    })
    void testMultiplyNullInputParameterized(BigDecimal a, BigDecimal b) {
        assertThrows(NullPointerException.class, () -> {
            calculatorService.multiply(a, b);
        });
    }

    @Test
    void testDivideBasic() {
        BigDecimal result = calculatorService.divide(new BigDecimal("10"), new BigDecimal("2"));
        assertTrue(result.compareTo(new BigDecimal("5")) == 0);
    }

    @Test
    void testDivideDecimal() {
        BigDecimal result = calculatorService.divide(new BigDecimal("5.5"), new BigDecimal("2.5"));
        assertTrue(result.compareTo(new BigDecimal("2.2")) == 0);
    }

    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> {
            calculatorService.divide(new BigDecimal("1"), BigDecimal.ZERO);
        });
    }

    @Test
    void testDivideNegative() {
        BigDecimal result = calculatorService.divide(new BigDecimal("-10"), new BigDecimal("-2"));
        assertTrue(result.compareTo(new BigDecimal("5")) == 0);
    }

    @Test
    void testDivideLargeNumbers() {
        // 32 Digits
        BigDecimal a = new BigDecimal("11111111111111111111111111111110");
        BigDecimal b = new BigDecimal("2");
        BigDecimal expected = new BigDecimal("5555555555555555555555555555555");
        BigDecimal result = calculatorService.divide(a, b);
        assertTrue(result.compareTo(expected) == 0);
    }

    @ParameterizedTest
    @CsvSource({
        ", 1",
        "1, ",
    })
    void testDivideNullInputParameterized(BigDecimal a, BigDecimal b) {
        assertThrows(NullPointerException.class, () -> {
            calculatorService.divide(a, b);
        });
    }

    @Test
    void testDividePrecision() {
        // 20 Digits results because of default
        BigDecimal result = calculatorService.divide(new BigDecimal("1"), new BigDecimal("3"));
        assertEquals(new BigDecimal("0.33333333333333333333").setScale(20, RoundingMode.HALF_UP), result);
    }
    
}
