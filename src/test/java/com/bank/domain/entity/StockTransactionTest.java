package com.bank.domain.entity;

import com.bank.domain.valueobject.OperationStatus;
import com.bank.domain.valueobject.Tax;
import com.bank.domain.valueobject.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockTransactionTest {

    private StockTransaction stockTransaction;

    @BeforeEach
    void setUp() {
        stockTransaction = new StockTransaction();
    }

    @Test
    @DisplayName("Test add operation with correct parameters")
    void testAddOperation() {
        //Given
        Double newWeightedAveragePrice = 20d;
        Double totalLoss = 30d;
        Long currentStockQuantity = 20l;
        Integer expectedSize = 1;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(50l).build();
        Tax tax = Tax.builder().tax(10d).build();
        OperationStatus operationStatus = OperationStatus.builder().stockOperation(stockOperation).tax(tax).build();

        //When
        stockTransaction = stockTransaction.addOperation(operationStatus, newWeightedAveragePrice, totalLoss, currentStockQuantity);

        //Then
        assertEquals(expectedSize, stockTransaction.getOperationList().size(), "The size of the transaction should be " + expectedSize);
        assertEquals(newWeightedAveragePrice, stockTransaction.getWeightedAveragePrice(), "The WeightedAveragePrice transaction should be " + newWeightedAveragePrice);
        assertEquals(currentStockQuantity, stockTransaction.getCurrentStockQuantity(), "The currentStockQuantity transaction should be " + currentStockQuantity);
        assertEquals(totalLoss, stockTransaction.getTotalLoss(), "The totalLoss transaction should be " + totalLoss);
    }

    @Test
    @DisplayName("Test add operation with correct parameters")
    void testAddOperationAddingMoreThan1Operation() {
        //Given
        Double newWeightedAveragePrice = 20d;
        Double totalLoss = 30d;
        Long currentStockQuantity = 20l;
        Integer expectedSize = 2;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(50l).build();
        Tax tax = Tax.builder().tax(10d).build();
        OperationStatus operationStatus = OperationStatus.builder().stockOperation(stockOperation).tax(tax).build();

        //When
        stockTransaction = stockTransaction.addOperation(operationStatus, newWeightedAveragePrice, totalLoss, currentStockQuantity);
        stockTransaction = stockTransaction.addOperation(operationStatus, newWeightedAveragePrice, totalLoss, currentStockQuantity);


        //Then
        assertEquals(expectedSize, stockTransaction.getOperationList().size(), "The size of the transaction should be " + expectedSize);
        assertEquals(newWeightedAveragePrice, stockTransaction.getWeightedAveragePrice(), "The WeightedAveragePrice transaction should be " + newWeightedAveragePrice);
        assertEquals(currentStockQuantity, stockTransaction.getCurrentStockQuantity(), "The currentStockQuantity transaction should be " + currentStockQuantity);
        assertEquals(totalLoss, stockTransaction.getTotalLoss(), "The totalLoss transaction should be " + totalLoss);
    }

    @Test
    void testAddOperationWhenStockMarketOperationIsNull() {
        //Given
        Double newWeightedAveragePrice = 20d;
        Double totalLoss = 30d;
        Long currentStockQuantity = 20l;

        //When
        assertThrows(IllegalArgumentException.class,
                () -> stockTransaction.addOperation(null, newWeightedAveragePrice, totalLoss, currentStockQuantity));

    }
}