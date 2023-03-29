package com.bank.domain.service;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.entity.StockTransaction;
import com.bank.domain.policy.StockOperationCalculator;
import com.bank.domain.policy.TaxesCalculator;
import com.bank.domain.specification.EnoughStockQuantitySpecification;
import com.bank.domain.specification.OperationShouldRecalculateWeightedAverage;
import com.bank.domain.valueobject.*;
import lombok.Builder;

import java.util.List;

/**
 * Main service to calculate the taxes given one operation
 */
@Builder
public class StockOperationService {

    private final TaxesCalculator taxesCalculator;
    private final StockOperationCalculator stockOperationCalculator;
    private final OperationShouldRecalculateWeightedAverage operationShouldRecalculateWeightedAverage;
    private final EnoughStockQuantitySpecification enoughStockQuantitySpecification;

    /**
     * Calculate all taxes for each transaction given a list of transaction
     *
     * @param stockOperationList the list of transactions
     * @return the list of taxes to pay
     */
    public StockTransaction processOperation(List<StockOperation> stockOperationList) {
        StockTransaction stockTransaction = new StockTransaction();
        for (StockOperation stockOperation :
                stockOperationList) {
            stockTransaction = calculateTaxesForAOperation(stockOperation, stockTransaction);
        }

        return stockTransaction;
    }

    private StockTransaction calculateTaxesForAOperation(StockOperation stockOperation, StockTransaction stockTransaction) {
        Long newStockQuantity = stockOperationCalculator.calculateNewStockQuantity(stockOperation, stockTransaction.getCurrentStockQuantity());

        if(!enoughStockQuantitySpecification.isSatisfiedBy(newStockQuantity)){
            AbstractEntityStatus operationStatus = ErrorStatus.builder().error("This operation can not be performance, not enough Stock").build();
            stockTransaction.addOperation(operationStatus, stockTransaction.getWeightedAveragePrice(), stockTransaction.getTotalLoss(), stockTransaction.getCurrentStockQuantity());
        }else {
            Double newWeightedAveragePrice = stockTransaction.getWeightedAveragePrice();
            if (operationShouldRecalculateWeightedAverage.isSatisfiedBy(stockOperation)) {
                newWeightedAveragePrice = stockOperationCalculator.calculateWeighedAverage(stockTransaction.getCurrentStockQuantity(), stockTransaction.getWeightedAveragePrice(), stockOperation.getQuantity(), stockOperation.getUnitCost());
            }
            OperationPerformance operationPerformance = stockOperationCalculator.calculatePerformance(stockOperation, stockTransaction.getWeightedAveragePrice());

            OperationPerformanceTotalLossTuple operationPerformanceTotalLossTuple = stockOperationCalculator.calculateNewTotalLoss(stockTransaction, operationPerformance, stockOperation);
            operationPerformance = operationPerformanceTotalLossTuple.getOperationPerformance();
            Double newTotalLoss = operationPerformanceTotalLossTuple.getTotalLoss();
            operationPerformance = stockOperationCalculator.calculatePerformanceWithTheNewTotalLoss(operationPerformance, newTotalLoss);

            Tax tax = taxesCalculator.calculateTaxToPay(operationPerformance, stockOperation);
            OperationStatus operationStatus = OperationStatus.builder().tax(tax).stockOperation(stockOperation).build();
            stockTransaction = stockTransaction.addOperation(operationStatus, newWeightedAveragePrice, newTotalLoss, newStockQuantity);
        }
        return stockTransaction;
    }

}
