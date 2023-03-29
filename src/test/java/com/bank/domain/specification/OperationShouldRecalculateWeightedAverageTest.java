package com.bank.domain.specification;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.valueobject.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationShouldRecalculateWeightedAverageTest {

    OperationShouldRecalculateWeightedAverage operationShouldRecalculateWeightedAverage;

    @BeforeEach
    void setUp() {
        operationShouldRecalculateWeightedAverage = OperationShouldRecalculateWeightedAverage.builder().build();
    }

    @Test
    @DisplayName("Test should recalculate Weighted Average when is a buy")
    void testWhenShouldRecalculateWeightedAverageIsSatisfiedByWhenIsABuy() {
        StockOperation operation = StockOperation.builder().operationType(OperationType.BUY).build();
        assertTrue(operationShouldRecalculateWeightedAverage.isSatisfiedBy(operation), "Should recalculate when is a Buy");
    }

    @Test
    @DisplayName("Test should not recalculate Weighted Average when is a Sell")
    void testWhenShouldNotRecalculateWeightedAverageIsSatisfiedByWhenIsASell() {
        StockOperation operation = StockOperation.builder().operationType(OperationType.SELL).build();
        assertFalse(operationShouldRecalculateWeightedAverage.isSatisfiedBy(operation), "Should not recalculate when is a Sell");
    }
}