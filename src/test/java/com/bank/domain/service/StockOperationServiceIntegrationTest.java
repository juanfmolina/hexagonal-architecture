package com.bank.domain.service;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.policy.StockOperationCalculator;
import com.bank.domain.policy.TaxesCalculator;
import com.bank.domain.specification.OperationShouldPayTaxesSpecification;
import com.bank.domain.specification.OperationShouldRecalculateWeightedAverage;
import com.bank.domain.specification.PerformanceShouldPayTaxSpecification;
import com.bank.domain.specification.StockQuantitySpecification;
import com.bank.domain.valueobject.OperationType;
import com.bank.domain.entity.StockTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class StockOperationServiceIntegrationTest {

    private StockOperationService taxOperationService;
    private TaxesCalculator taxesCalculator;
    private StockOperationCalculator stockOperationCalculator;
    private OperationShouldRecalculateWeightedAverage operationShouldRecalculateWeightedAverage;
    private OperationShouldPayTaxesSpecification operationShouldPayTaxesSpecification;
    private PerformanceShouldPayTaxSpecification performanceShouldPayTaxSpecification;
    private StockQuantitySpecification stockQuantitySpecification;

    @BeforeEach
    void setUp(){
        //Creating all the specifications
        operationShouldPayTaxesSpecification = OperationShouldPayTaxesSpecification.builder().build();
        operationShouldRecalculateWeightedAverage = OperationShouldRecalculateWeightedAverage.builder().build();
        performanceShouldPayTaxSpecification = PerformanceShouldPayTaxSpecification.builder().build();
        stockQuantitySpecification = StockQuantitySpecification.builder().build();

        //Creating policies
        taxesCalculator = TaxesCalculator.builder().performanceShouldPayTaxSpecification(performanceShouldPayTaxSpecification)
                .operationShouldPayTaxesSpecification(operationShouldPayTaxesSpecification).build();
        stockOperationCalculator = StockOperationCalculator.builder().stockQuantitySpecification(stockQuantitySpecification)
                .operationShouldPayTaxesSpecification(operationShouldPayTaxesSpecification).build();
        taxOperationService = StockOperationService.builder().stockOperationCalculator(stockOperationCalculator)
                .taxesCalculator(taxesCalculator).operationShouldRecalculateWeightedAverage(operationShouldRecalculateWeightedAverage).build();
    }

    //@Test
    @DisplayName("Case #1")
    void testCalculateTaxesTestCase1() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(100l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15d).quantity(50l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15d).quantity(50l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Total amount less than $ 20,000");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Total amount less than $ 20,000");
    }

    //@Test
    @DisplayName("Case #2")
    void testCalculateTaxesTestCase2() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(5000l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(5d).quantity(5000l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(10000, stockTransaction.getOperationList().get(1).getTax().getTax(), "Profit of $ 50,000: 20% of taxes is $ 10,000 and there is no previous losses to use");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Loss of $ 25,000: no tax");
    }

    //@Test
    @DisplayName("Case #2 + Case #3")
    void testCalculateTaxesTestCase2_3() {

        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(100l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15d).quantity(50l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15d).quantity(50l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Total amount less than $ 20,000");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Total amount less than $ 20,000");


        //Given
        StockOperation stockOperation3 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation4 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(5000l).build();
        StockOperation stockOperation5 = StockOperation.builder().operationType(OperationType.SELL).unitCost(5d).quantity(5000l).build();
        operationList = List.of(stockOperation3, stockOperation4, stockOperation5);

        //When
        StockTransaction stockTransaction2 = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction2.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction2.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(10000, stockTransaction2.getOperationList().get(1).getTax().getTax(), "Profit of $ 50,000: 20% of taxes is $ 10,000 and there is no previous losses to use");
        assertEquals(0, stockTransaction2.getOperationList().get(2).getTax().getTax(), "Loss of $ 25,000: no tax");
    }

    //@Test
    @DisplayName("Case #3")
    void testCalculateTaxesTestCase3() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(5d).quantity(5000l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(3000l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Loss of $ 25,000: no taxes");
        assertEquals(1000, stockTransaction.getOperationList().get(2).getTax().getTax(), "Profit of $ 30,000: deduct Loss of $ 25,000 and pay 20% of$ 5,000 in taxes ($ 1,000)");
    }

    //@Test
    @DisplayName("Case #4")
    void testCalculateTaxesTestCase4() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.BUY).unitCost(25d).quantity(5000l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15d).quantity(10000l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Considering average price of $ 15 ((10×10,000 + 25×5,000)÷ 15,000) there is no profit or loss");
    }

    //@Test
    @DisplayName("Case #5")
    void testCalculateTaxesTestCase5() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.BUY).unitCost(25d).quantity(5000l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15d).quantity(10000l).build();
        StockOperation stockOperation3 = StockOperation.builder().operationType(OperationType.SELL).unitCost(25d).quantity(5000l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2, stockOperation3);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Considering average price of $ 15 ((10×10,000 + 25×5,000)÷ 15,000) there is no profit or loss");
        assertEquals(10000, stockTransaction.getOperationList().get(3).getTax().getTax(), "Considering average price of $ 15 profit of $ 50,000: pay 20% of $ 50,000 in taxes ($ 10,000)");
    }

    //@Test
    @DisplayName("Case #6")
    void testCalculateTaxesTestCase6() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(2d).quantity(5000l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(2000l).build();
        StockOperation stockOperation3 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(2000l).build();
        StockOperation stockOperation4 = StockOperation.builder().operationType(OperationType.SELL).unitCost(25d).quantity(1000l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2, stockOperation3, stockOperation4);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Loss of $ 40,000: total amount is less than $ 20,000, but we should deduct that loss regardless of that");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Profit of $ 20,000: if you deduct all the loss, profit is zero.Still have $ 20,000 of loss to deduct");
        assertEquals(0, stockTransaction.getOperationList().get(3).getTax().getTax(), "Profit of $ 20,000: if you deduct all the loss, profit is zero.Now there is no loss to deduct");
        assertEquals(3000, stockTransaction.getOperationList().get(4).getTax().getTax(), "Profit of $ 15,000 and zero losses to deduct: pay 20% of $15,000 in taxes ($ 3,000)");
    }

    //@Test
    @DisplayName("Case #7")
    void testCalculateTaxesTestCase7() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(2d).quantity(5000l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(2000l).build();
        StockOperation stockOperation3 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20d).quantity(2000l).build();
        StockOperation stockOperation4 = StockOperation.builder().operationType(OperationType.SELL).unitCost(25d).quantity(1000l).build();
        StockOperation stockOperation5 = StockOperation.builder().operationType(OperationType.BUY).unitCost(20d).quantity(10000l).build();
        StockOperation stockOperation6 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15d).quantity(5000l).build();
        StockOperation stockOperation7 = StockOperation.builder().operationType(OperationType.SELL).unitCost(30d).quantity(4350l).build();
        StockOperation stockOperation8 = StockOperation.builder().operationType(OperationType.SELL).unitCost(30d).quantity(650l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2, stockOperation3, stockOperation4,
                stockOperation5, stockOperation6, stockOperation7, stockOperation8);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Loss of $ 40,000: total amount is less than $ 20,000, but we should deduct that loss regardless of that");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Profit of $ 20,000: if you deduct all the loss, profit is zero.Still have $ 20,000 of loss to deduct");
        assertEquals(0, stockTransaction.getOperationList().get(3).getTax().getTax(), "Profit of $ 20,000: if you deduct all the loss, profit is zero.Now there is no loss to deduct");
        assertEquals(3000, stockTransaction.getOperationList().get(4).getTax().getTax(), "Profit of $ 15,000 and zero losses to deduct: pay 20% of $15,000 in taxes ($ 3,000)");
        assertEquals(0, stockTransaction.getOperationList().get(5).getTax().getTax(), "All stocks were sold. Buying new ones changes the average price to the paid price of the new stocks ($ 20)");
        assertEquals(0, stockTransaction.getOperationList().get(6).getTax().getTax(), "Loss of $ 25,000");
        assertEquals(3700, stockTransaction.getOperationList().get(7).getTax().getTax(), "Profit of $ 43,500: if you deduct the loss of $ 25,000, there is$ 18,500 of profit left. Pay 20% of $ 18,500 in taxes ($3,700)");
        assertEquals(0, stockTransaction.getOperationList().get(8).getTax().getTax(), "Profit of $ 6,500: no loss to deduct, but the total amount is less than $ 20,000");
    }

    //@Test
    @DisplayName("Case #8")
    void testCalculateTaxesTestCase8() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(10d).quantity(10000l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(50d).quantity(10000l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.BUY).unitCost(20d).quantity(10000l).build();
        StockOperation stockOperation3 = StockOperation.builder().operationType(OperationType.SELL).unitCost(50d).quantity(10000l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2, stockOperation3);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(80000, stockTransaction.getOperationList().get(1).getTax().getTax(), "Profit of $ 400,000: pay 20% of $ 400,000 in taxes ($80,000)");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(60000, stockTransaction.getOperationList().get(3).getTax().getTax(), "Profit of $ 300,000: pay 20% of $ 300,000 in taxes ($60,000)");
    }

    //@Test
    @DisplayName("Case #9")
    void testCalculateTaxesTestCase9() {
        //Given
        StockOperation stockOperation0 = StockOperation.builder().operationType(OperationType.BUY).unitCost(5000d).quantity(10l).build();
        StockOperation stockOperation1 = StockOperation.builder().operationType(OperationType.SELL).unitCost(4000d).quantity(5l).build();
        StockOperation stockOperation2 = StockOperation.builder().operationType(OperationType.BUY).unitCost(15000d).quantity(5l).build();
        StockOperation stockOperation3 = StockOperation.builder().operationType(OperationType.BUY).unitCost(4000d).quantity(2l).build();
        StockOperation stockOperation4 = StockOperation.builder().operationType(OperationType.BUY).unitCost(23000d).quantity(2l).build();
        StockOperation stockOperation5 = StockOperation.builder().operationType(OperationType.SELL).unitCost(20000d).quantity(1l).build();
        StockOperation stockOperation6 = StockOperation.builder().operationType(OperationType.SELL).unitCost(12000d).quantity(10l).build();
        StockOperation stockOperation7 = StockOperation.builder().operationType(OperationType.SELL).unitCost(15000d).quantity(3l).build();
        List<StockOperation> operationList = List.of(stockOperation0, stockOperation1, stockOperation2, stockOperation3, stockOperation4,
                stockOperation5, stockOperation6, stockOperation7);

        //When
        StockTransaction stockTransaction = taxOperationService.processOperation(operationList);

        //Then
        assertEquals(operationList.size(), stockTransaction.getOperationList().size(), "The size of the tax list should be the same as the operation list");
        assertEquals(0, stockTransaction.getOperationList().get(0).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(1).getTax().getTax(), "Loss of $5,000: total amount is equal $ 20,000, but we should deduct that loss regardless of that");
        assertEquals(0, stockTransaction.getOperationList().get(2).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(3).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(4).getTax().getTax(), "Buying stocks do not pay taxes");
        assertEquals(0, stockTransaction.getOperationList().get(5).getTax().getTax(), "Total operation amount <= $ 20,000: it does not pay taxes nor touches the losses");
        assertEquals(1000, stockTransaction.getOperationList().get(6).getTax().getTax(), "Profit of $ 10,000: if you deduct the loss of $ 5,000, there is $ 5,000 of profit left. Pay 20% of $ 5,000 in taxes ($1,000)");
        assertEquals(2400, stockTransaction.getOperationList().get(7).getTax().getTax(), "Profit of $ 12,000 and zero losses to deduct: Pay 20% of$ 12,000 in taxes ($ 2,400)");
    }


}