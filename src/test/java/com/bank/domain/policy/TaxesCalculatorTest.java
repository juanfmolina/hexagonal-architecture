package com.bank.domain.policy;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.specification.OperationShouldPayTaxesSpecification;
import com.bank.domain.specification.PerformanceShouldPayTaxSpecification;
import com.bank.domain.valueobject.OperationPerformance;
import com.bank.domain.valueobject.OperationPerformanceType;
import com.bank.domain.valueobject.OperationType;
import com.bank.domain.valueobject.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaxesCalculatorTest {

    private TaxesCalculator taxesCalculator;
    private OperationShouldPayTaxesSpecification operationShouldPayTaxesSpecification;
    private PerformanceShouldPayTaxSpecification performanceShouldPayTaxSpecification;

    @BeforeEach
    void setUp() {
        operationShouldPayTaxesSpecification = mock(OperationShouldPayTaxesSpecification.class);
        performanceShouldPayTaxSpecification = mock(PerformanceShouldPayTaxSpecification.class);
        taxesCalculator = new TaxesCalculator(performanceShouldPayTaxSpecification, operationShouldPayTaxesSpecification);
    }

    @Test
    @DisplayName("Calculating the tax to pay given a Profit performance")
    void testCalculateTaxToPayWhenIsAProfit() {
        //GIVEN
        Double overallProfit = 50000d;

        StockOperation operation = StockOperation.builder().operationType(OperationType.SELL).quantity(10000l).unitCost(200d).build();

        OperationPerformance profitOperationPerformance = OperationPerformance.builder().performanceType(OperationPerformanceType.PROFIT).amount(overallProfit).build();

        Double expectedTaxToPay = 10000d;

        //WHEN
        when(operationShouldPayTaxesSpecification.isSatisfiedBy(any(StockOperation.class))).thenReturn(true);
        when(performanceShouldPayTaxSpecification.isSatisfiedBy(any(OperationPerformance.class))).thenReturn(true);
        Tax taxToPay = taxesCalculator.calculateTaxToPay(profitOperationPerformance, operation);

        //Then
        assertEquals(expectedTaxToPay, taxToPay.getTax(), "Should calculate 10000 as the amount of tax to pay");
    }

    @Test
    @DisplayName("Calculating the tax to pay given a Loss performance")
    void testCalculateTaxToPayWhenIsALoss() {
        //GIVEN
        Double overallProfit = -50000d;

        OperationPerformance profitOperationPerformance = OperationPerformance.builder().performanceType(OperationPerformanceType.LOSS).amount(overallProfit).build();
        StockOperation operation = StockOperation.builder().operationType(OperationType.SELL).quantity(10000l).unitCost(200d).build();

        Double expectedTaxToPay = 0d;

        //WHEN
        Tax taxToPay = taxesCalculator.calculateTaxToPay(profitOperationPerformance, operation);

        //Then
        assertEquals(expectedTaxToPay, taxToPay.getTax(), "Should calculate 0 as the amount of tax to pay");
    }

    @Test
    @DisplayName("Calculating the tax to pay when performance should not pay tax")
    void testCalculateTaxToPayWhenPerformanceShouldNotPayTax() {
        //GIVEN
        Double overallProfit = 50000d;

        OperationPerformance profitOperationPerformance = OperationPerformance.builder().performanceType(OperationPerformanceType.PROFIT).amount(overallProfit).build();
        StockOperation operation = StockOperation.builder().operationType(OperationType.SELL).quantity(10000l).unitCost(200d).build();

        Double expectedTaxToPay = 0d;

        //WHEN
        when(performanceShouldPayTaxSpecification.isSatisfiedBy(profitOperationPerformance)).thenReturn(false);
        Tax taxToPay = taxesCalculator.calculateTaxToPay(profitOperationPerformance, operation);

        //Then
        assertEquals(expectedTaxToPay, taxToPay.getTax(), "Should calculate 0 as the amount of tax to pay");
    }

    @Test
    @DisplayName("Calculating the tax to pay when operation should not pay tax")
    void testCalculateTaxToPayWhenOperationShouldNotPayTax() {
        //GIVEN
        Double overallProfit = 50000d;

        OperationPerformance profitOperationPerformance = OperationPerformance.builder().performanceType(OperationPerformanceType.PROFIT).amount(overallProfit).build();
        StockOperation operation = StockOperation.builder().operationType(OperationType.SELL).quantity(10000l).unitCost(200d).build();

        Double expectedTaxToPay = 0d;

        //WHEN
        when(operationShouldPayTaxesSpecification.isSatisfiedBy(operation)).thenReturn(false);
        Tax taxToPay = taxesCalculator.calculateTaxToPay(profitOperationPerformance, operation);

        //Then
        assertEquals(expectedTaxToPay, taxToPay.getTax(), "Should calculate 0 as the amount of tax to pay");
    }

    @Test
    @DisplayName("Calculating the tax to pay when overall profit is null")
    void testCalculateTaxToPayWhenTheOverallProfitIsNull() {
        //GIVEN
        Double expectedTaxToPay = 0d;

        //WHEN
        Tax taxToPay = taxesCalculator.calculateTaxToPay(null, null);

        //Then
        assertEquals(expectedTaxToPay, taxToPay.getTax(), "Should calculate 0 as the amount of tax to pay when the overall profit is null");
    }
}