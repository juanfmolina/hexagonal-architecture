package com.bank.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationTypeTest {

    @Test
    @DisplayName("Get Operation when is a sell")
    void testGetOperationByNameWhenIsASell() {
        assertEquals(OperationType.SELL, OperationType.getOperationTypeByName("sell"));
    }

    @Test
    @DisplayName("Get Operation when is a buy")
    void testGetOperationByNameWhenIsABuy() {
        assertEquals(OperationType.BUY, OperationType.getOperationTypeByName("buy"));
    }

    @Test
    @DisplayName("Get Operation exception when is a not a known operation")
    void testGetOperationByNameWhenIsANotAKnownOperation() {
        assertThrows(IllegalArgumentException.class,() -> OperationType.getOperationTypeByName("anotherOperation"));
    }
}