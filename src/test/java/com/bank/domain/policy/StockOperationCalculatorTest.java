package com.bank.domain.policy;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.specification.OperationShouldPayTaxesSpecification;
import com.bank.domain.specification.StockQuantitySpecification;
import com.bank.domain.valueobject.OperationPerformance;
import com.bank.domain.valueobject.OperationPerformanceType;
import com.bank.domain.valueobject.OperationType;
import com.bank.domain.entity.StockTransaction;
import com.bank.domain.valueobject.OperationPerformanceTotalLossTuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StockOperationCalculatorTest {

    StockOperationCalculator stockOperationCalculator;
    StockQuantitySpecification stockQuantitySpecification;
    OperationShouldPayTaxesSpecification operationShouldPayTaxesSpecification;


    @BeforeEach
    void setUp() {
        stockQuantitySpecification = mock(StockQuantitySpecification.class);
        operationShouldPayTaxesSpecification = mock(OperationShouldPayTaxesSpecification.class);
        stockOperationCalculator = StockOperationCalculator.builder().stockQuantitySpecification(stockQuantitySpecification)
                .operationShouldPayTaxesSpecification(operationShouldPayTaxesSpecification).build();
    }

    @Test
    @DisplayName("Calculated the weighed average")
    void testCalculateWeighedAverage() {
        //Given
        Long currentStockQuantity = 5l;
        Double weightedAveragePrice = 20d;
        Long newStockQuantity = 5l;
        Double newStockPrice = 10d;
        Double expectedNewWeightedAverage = 15d;

        //When
        when(stockQuantitySpecification.isSatisfiedBy(anyLong())).thenReturn(true);
        Double newWeighedAverage = stockOperationCalculator.calculateWeighedAverage(currentStockQuantity, weightedAveragePrice, newStockQuantity, newStockPrice);

        //Then
        assertEquals(newWeighedAverage, expectedNewWeightedAverage, "New weighted-average should be 15.00");

    }

    @Test
    @DisplayName("Calculated the weighed average when parameter are not correct")
    void testCalculateWeighedAverageThrowsAnException() {
        //Given
        Long currentStockQuantity = 0l;
        Double weightedAveragePrice = 20d;
        Long newStockQuantity = 0l;
        Double newStockPrice = 10d;
        Double expectedNewWeightedAverage = 15d;

        when(stockQuantitySpecification.isSatisfiedBy(anyLong())).thenReturn(false);
        //When
        assertThrows(IllegalArgumentException.class,
                () -> stockOperationCalculator.calculateWeighedAverage(
                        currentStockQuantity, weightedAveragePrice, newStockQuantity, newStockPrice), "Should throws an exception");

    }

    @Test
    @DisplayName("Calculate the performance of a operation when is a profit")
    void testCalculateAPerformanceWhenIsAProfit(){
        //Given
        Double weightedAveragePrice = 10d;
        Long newStockQuantity = 5000l;
        Double newStockPrice = 20d;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).quantity(newStockQuantity).unitCost(newStockPrice).build();
        Double expectedProfit = 50000d;
        OperationPerformanceType expectedType = OperationPerformanceType.PROFIT;

        //When
        OperationPerformance performance = stockOperationCalculator.calculatePerformance(stockOperation, weightedAveragePrice);

        //Then
        assertEquals(expectedProfit, performance.getAmount(), "Should profit " + expectedProfit);
        assertEquals(expectedType, performance.getPerformanceType(), "Should be a Profit");
    }

    @Test
    @DisplayName("Calculate the performance of a operation when is a loss")
    void testCalculateAPerformanceWhenIsALoss(){
        //Given
        Double weightedAveragePrice = 10d;
        Long newStockQuantity = 5000l;
        Double newStockPrice = 5d;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).quantity(newStockQuantity).unitCost(newStockPrice).build();
        Double expectedLoss = -25000d;
        OperationPerformanceType expectedType = OperationPerformanceType.LOSS;

        //When
        OperationPerformance performance = stockOperationCalculator.calculatePerformance(stockOperation, weightedAveragePrice);

        //Then
        assertEquals(expectedLoss, performance.getAmount(), "Should loss " + expectedLoss);
        assertEquals(expectedType, performance.getPerformanceType(), "Should be a Loss");
    }


    @Test
    @DisplayName("Calculate the new total loss of a operation when is performance is loss")
    void testCalculateTotalLossWhenPerformanceIsLoss(){
        //Given
        Long newStockQuantity = 5000l;
        Double newStockPrice = 5d;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).quantity(newStockQuantity).unitCost(newStockPrice).build();
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.LOSS).amount(-5000d).build();
        StockTransaction stockTransaction = mock(StockTransaction.class);
        Double currentLoss = -25000d;
        Double expectedLoss = -30000d;

        //When
        when(stockTransaction.getTotalLoss()).thenReturn(currentLoss);
        OperationPerformanceTotalLossTuple performanceTotalLossTuple = stockOperationCalculator.calculateNewTotalLoss(stockTransaction, performance, stockOperation);

        //Then
        assertEquals(expectedLoss, performanceTotalLossTuple.getTotalLoss(), "Should loss " + expectedLoss);
        assertEquals(performance, performanceTotalLossTuple.getOperationPerformance(), "Should be the same performance");
    }

    @Test
    @DisplayName("Calculate the new total loss of a operation when is performance is profit")
    void testCalculateTotalLossWhenPerformanceIsProfit(){
        //Given
        Long newStockQuantity = 5000l;
        Double newStockPrice = 5d;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).quantity(newStockQuantity).unitCost(newStockPrice).build();
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.PROFIT).amount(5000d).build();
        StockTransaction stockTransaction = mock(StockTransaction.class);
        Double currentLoss = -25000d;
        Double expectedLoss = -20000d;


        //When
        when(stockTransaction.getTotalLoss()).thenReturn(currentLoss);
        when(operationShouldPayTaxesSpecification.isSatisfiedBy(stockOperation)).thenReturn(true);
        OperationPerformanceTotalLossTuple performanceTotalLossTuple = stockOperationCalculator.calculateNewTotalLoss(stockTransaction, performance, stockOperation);

        //Then
        assertEquals(expectedLoss, performanceTotalLossTuple.getTotalLoss(), "Should loss " + expectedLoss);
        assertEquals(0, performanceTotalLossTuple.getOperationPerformance().getAmount(), "Should be Zero profit");
    }

    @Test
    @DisplayName("Calculate the new total loss of a operation when is performance is profit")
    void testCalculateTotalLossWhenPerformanceIsProfitAndTotalLossIsLessThanProfit(){
        //Given
        Long newStockQuantity = 5000l;
        Double newStockPrice = 5d;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).quantity(newStockQuantity).unitCost(newStockPrice).build();
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.PROFIT).amount(25000d).build();
        StockTransaction stockTransaction = mock(StockTransaction.class);
        Double currentLoss = -20000d;
        Double expectedLoss = 0d;


        //When
        when(stockTransaction.getTotalLoss()).thenReturn(currentLoss);
        when(operationShouldPayTaxesSpecification.isSatisfiedBy(stockOperation)).thenReturn(true);
        OperationPerformanceTotalLossTuple performanceTotalLossTuple = stockOperationCalculator.calculateNewTotalLoss(stockTransaction, performance, stockOperation);

        //Then
        assertEquals(expectedLoss, performanceTotalLossTuple.getTotalLoss(), "Should loss " + expectedLoss);
        assertEquals(5000, performanceTotalLossTuple.getOperationPerformance().getAmount(), "Should be Zero profit");
        assertTrue(OperationPerformance.isProfit().test(performanceTotalLossTuple.getOperationPerformance()));
    }

    @Test
    @DisplayName("Calculate the new stock quantity when is a sell")
    void testCalculateStockQuantityWhenIsASell(){
        //Given
        Long newStockQuantity = 5000l;
        Double newStockPrice = 5d;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.SELL).quantity(newStockQuantity).unitCost(newStockPrice).build();
        Long currentQuantity = 5000l;
        Long expectedQuantity = 0l;


        //When
        stockOperationCalculator.calculateNewStockQuantity(stockOperation, currentQuantity);

        //Then
        assertEquals(expectedQuantity, stockOperationCalculator.calculateNewStockQuantity(stockOperation, currentQuantity), "Expected Quantity should be " + expectedQuantity);
    }

    @Test
    @DisplayName("Calculate the new stock quantity when is a buy")
    void testCalculateStockQuantityWhenIsABuy(){
        //Given
        Long newStockQuantity = 5000l;
        Double newStockPrice = 5d;
        StockOperation stockOperation = StockOperation.builder().operationType(OperationType.BUY).quantity(newStockQuantity).unitCost(newStockPrice).build();
        Long currentQuantity = 5000l;
        Long expectedQuantity = 10000l;


        //When
        stockOperationCalculator.calculateNewStockQuantity(stockOperation, currentQuantity);

        //Then
        assertEquals(expectedQuantity, stockOperationCalculator.calculateNewStockQuantity(stockOperation, currentQuantity), "Expected Quantity should be " + expectedQuantity);
    }

    @Test
    @DisplayName("Calculate the new total loss of a operation when performance is profit")
    void testCalculatePerformanceWhiAndExistingTotalLossAndItsAProfit(){
        //Given
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.PROFIT).amount(25000d).build();
        Double currentLoss = -20000d;
        Double expectedPerformance = 5000d;
        //When
        OperationPerformance currentPerformance = stockOperationCalculator.calculatePerformanceWithTheNewTotalLoss(performance, currentLoss);

        //Then
        assertEquals(expectedPerformance, currentPerformance.getAmount(), "Should profit " + expectedPerformance);
        assertTrue(OperationPerformance.isProfit().test(currentPerformance));
    }

    @Test
    @DisplayName("Calculate the new total loss of a operation when performance is loss")
    void testCalculatePerformanceWhiAndExistingTotalLossAndItsALoss(){
        //Given
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.LOSS).amount(-25000d).build();
        Double currentLoss = -20000d;
        Double expectedPerformance = performance.getAmount();
        //When
        OperationPerformance currentPerformance = stockOperationCalculator.calculatePerformanceWithTheNewTotalLoss(performance, currentLoss);

        //Then
        assertEquals(expectedPerformance, currentPerformance.getAmount(), "Should profit " + expectedPerformance);
        assertEquals(performance, currentPerformance, "Should be the same");
        assertTrue(OperationPerformance.isLoss().test(currentPerformance));
    }




}