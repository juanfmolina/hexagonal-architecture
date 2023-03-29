package com.bank.domain.service;

import com.bank.capitalgain.domain.valueobject.*;
import com.bank.domain.entity.StockOperation;
import com.bank.domain.entity.StockTransaction;
import com.bank.domain.policy.StockOperationCalculator;
import com.bank.domain.policy.TaxesCalculator;
import com.bank.domain.specification.OperationShouldRecalculateWeightedAverage;
import com.bank.domain.valueobject.*;
import com.br.nubank.capitalgain.domain.valueobject.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class StockOperationServiceTest {

    private StockOperationService taxOperationService;
    private TaxesCalculator taxesCalculator;
    private StockOperationCalculator stockOperationCalculator;
    private OperationShouldRecalculateWeightedAverage operationShouldRecalculateWeightedAverage;

    @BeforeEach
    void setUp(){
        taxesCalculator = mock(TaxesCalculator.class);
        stockOperationCalculator = mock(StockOperationCalculator.class);
        operationShouldRecalculateWeightedAverage = mock(OperationShouldRecalculateWeightedAverage.class);
        taxOperationService = StockOperationService.builder().stockOperationCalculator(stockOperationCalculator)
                .taxesCalculator(taxesCalculator).operationShouldRecalculateWeightedAverage(operationShouldRecalculateWeightedAverage).build();
    }

    @AfterEach
    void tearDown(){
        validateMockitoUsage();
    }

    @Test
    @DisplayName("Calculate Taxes with all conditions")
    void testCalculateTaxes() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(100l).build();
        List<StockOperation> operationList = List.of(stockOperation0);
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.LOSS).amount(10d).build();
        OperationPerformanceTotalLossTuple operationPerformanceTotalLossTuple = OperationPerformanceTotalLossTuple.builder().operationPerformance(performance).totalLoss(10d).build();
        Tax tax = Tax.builder().tax(0d).build();

        //When
        when(stockOperationCalculator.calculatePerformance(any(StockOperation.class), anyDouble())).thenReturn(performance);
        when(taxesCalculator.calculateTaxToPay(any(OperationPerformance.class), any(StockOperation.class))).thenReturn(tax);
        when(operationShouldRecalculateWeightedAverage.isSatisfiedBy(any(StockOperation.class))).thenReturn(true);
        when(stockOperationCalculator.calculateNewStockQuantity(any(StockOperation.class), anyLong())).thenReturn(10l);
        when(stockOperationCalculator.calculateNewTotalLoss(any(StockTransaction.class), any(OperationPerformance.class), any(StockOperation.class))).thenReturn(operationPerformanceTotalLossTuple);
        when(stockOperationCalculator.calculatePerformanceWithTheNewTotalLoss(any(OperationPerformance.class), anyDouble())).thenReturn(performance);
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        verify(stockOperationCalculator).calculatePerformance(eq(stockOperation0), anyDouble());
        verify(taxesCalculator).calculateTaxToPay(eq(performance), eq(stockOperation0));
        verify(stockOperationCalculator).calculateNewStockQuantity(eq(stockOperation0), anyLong());
        verify(stockOperationCalculator).calculateWeighedAverage(anyLong(), anyDouble(), anyLong(), anyDouble());
        verify(stockOperationCalculator).calculateNewTotalLoss(any(StockTransaction.class), eq(performance), any(StockOperation.class));
        verify(stockOperationCalculator).calculatePerformanceWithTheNewTotalLoss(any(OperationPerformance.class), anyDouble());

    }

    @Test
    @DisplayName("Calculate Taxes")
    void testCalculateTaxesWhenShouldNotCalculateWeightedAveragePrice() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(100l).build();
        List<StockOperation> operationList = List.of(stockOperation0);
        OperationPerformance performance = OperationPerformance.builder().performanceType(OperationPerformanceType.LOSS).amount(10d).build();
        OperationPerformanceTotalLossTuple operationPerformanceTotalLossTuple = OperationPerformanceTotalLossTuple.builder().operationPerformance(performance).totalLoss(10d).build();
        Tax tax = Tax.builder().tax(0d).build();

        //When
        when(stockOperationCalculator.calculatePerformance(any(StockOperation.class), anyDouble())).thenReturn(performance);
        when(taxesCalculator.calculateTaxToPay(any(OperationPerformance.class), any(StockOperation.class))).thenReturn(tax);
        when(operationShouldRecalculateWeightedAverage.isSatisfiedBy(any(StockOperation.class))).thenReturn(false);
        when(stockOperationCalculator.calculateNewStockQuantity(any(StockOperation.class), anyLong())).thenReturn(10l);
        when(stockOperationCalculator.calculateNewTotalLoss(any(StockTransaction.class), any(OperationPerformance.class), any(StockOperation.class))).thenReturn(operationPerformanceTotalLossTuple);
        when(stockOperationCalculator.calculatePerformanceWithTheNewTotalLoss(any(OperationPerformance.class), anyDouble())).thenReturn(performance);
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        verify(stockOperationCalculator).calculatePerformance(eq(stockOperation0), anyDouble());
        verify(taxesCalculator).calculateTaxToPay(eq(performance), eq(stockOperation0));
        verify(stockOperationCalculator).calculateNewStockQuantity(eq(stockOperation0), anyLong());
        verify(stockOperationCalculator, never()).calculateWeighedAverage(anyLong(), anyDouble(), anyLong(), anyDouble());
        verify(stockOperationCalculator).calculateNewTotalLoss(any(StockTransaction.class), eq(performance), eq(stockOperation0));
        verify(stockOperationCalculator).calculatePerformanceWithTheNewTotalLoss(any(OperationPerformance.class), anyDouble());
    }
}