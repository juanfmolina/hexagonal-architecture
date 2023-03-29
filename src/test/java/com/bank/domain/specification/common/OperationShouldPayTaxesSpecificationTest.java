package com.bank.domain.specification.common;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.specification.OperationShouldPayTaxesSpecification;
import com.bank.domain.valueobject.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationShouldPayTaxesSpecificationTest {

    OperationShouldPayTaxesSpecification operationShouldPayTaxesSpecification;

    @BeforeEach
    void setUp() {
        operationShouldPayTaxesSpecification = OperationShouldPayTaxesSpecification.builder().build();
    }

    @Test
    @DisplayName("The operation should pay taxes, if it meets all the conditions")
    void testOperationShouldPayTaxSatisfyingSpecification() {
        StockOperation operationShouldPayTax = StockOperation.builder().operationType(OperationType.SELL).quantity(10l).unitCost(10000d).build();
        assertTrue(operationShouldPayTaxesSpecification.isSatisfiedBy(operationShouldPayTax), "The operation should pay taxes");
    }

    @Test
    @DisplayName("The operation should not pay taxes when operation is not a sell")
    void testOperationShouldNotPayTaxSatisfyingSpecificationWhenIsNotASell() {
        StockOperation operationShouldPayTax = StockOperation.builder().operationType(OperationType.BUY).quantity(10l).unitCost(10000d).build();
        assertFalse(operationShouldPayTaxesSpecification.isSatisfiedBy(operationShouldPayTax), "The operation not should pay taxes");
    }

    @Test
    @DisplayName("The operation should not pay taxes when operation is a sell but does not meet amount")
    void testOperationShouldNotPayTaxSatisfyingSpecificationWhenIsASellAndDoesNotMeetAmount() {
        StockOperation operationShouldPayTax = StockOperation.builder().operationType(OperationType.SELL).quantity(1l).unitCost(10000d).build();
        assertFalse(operationShouldPayTaxesSpecification.isSatisfiedBy(operationShouldPayTax), "The operation not should pay taxes");
    }
}