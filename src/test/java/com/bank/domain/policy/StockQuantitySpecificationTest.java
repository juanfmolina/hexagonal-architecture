package com.bank.domain.policy;

import com.bank.domain.specification.StockQuantitySpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockQuantitySpecificationTest {

    private StockQuantitySpecification stockQuantitySpecification;

    @BeforeEach
    void setUp() {
        stockQuantitySpecification = StockQuantitySpecification.builder().build();
    }

    @Test
    @DisplayName("Zero stock should not satisfy the quantity")
    void testStockQuantityIsEqualToZeroShouldNotSatisfy() {
        Long stockQuantity = 0l;
        assertFalse(stockQuantitySpecification.isSatisfiedBy(stockQuantity));
    }

    @Test
    @DisplayName("Negative stock should not satisfy the quantity")
    void testStockQuantityIsLessThanZeroShouldNotSatisfy() {
        Long stockQuantity = -60l;
        assertFalse(stockQuantitySpecification.isSatisfiedBy(stockQuantity));
    }

    @Test
    @DisplayName("Positive stock should satisfy the quantity")
    void testStockQuantityIsMoreThanZeroShouldNotSatisfy() {
        Long stockQuantity = 60l;
        assertTrue(stockQuantitySpecification.isSatisfiedBy(stockQuantity));
    }
}