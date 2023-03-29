package com.bank.domain.specification;

import com.bank.domain.valueobject.OperationPerformance;
import com.bank.domain.valueobject.OperationPerformanceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceShouldPayTaxSpecificationTest {
    PerformanceShouldPayTaxSpecification performanceShouldPayTaxSpecification;

    @BeforeEach
    void setUp() {
        performanceShouldPayTaxSpecification = PerformanceShouldPayTaxSpecification.builder().build();
    }

    @Test
    @DisplayName("Test should pay when performance is profit")
    void testWhenShouldPayIsSatisfiedByWhenIsProfit() {
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.PROFIT).build();
        assertTrue(performanceShouldPayTaxSpecification.isSatisfiedBy(performance), "Should pay when is profit");
    }

    @Test
    @DisplayName("Test should pay when performance is loss")
    void testWhenShouldNotPayIsSatisfiedByWhenIsALoss() {
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.LOSS).build();
        assertFalse(performanceShouldPayTaxSpecification.isSatisfiedBy(performance), "Should not pay when is loss");
    }
}