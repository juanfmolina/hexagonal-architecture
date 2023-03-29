package com.bank.domain.policy;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.specification.OperationShouldPayTaxesSpecification;
import com.bank.domain.specification.StockQuantitySpecification;
import com.bank.domain.valueobject.OperationPerformance;
import com.bank.domain.valueobject.OperationPerformanceType;
import com.bank.domain.valueobject.OperationPerformanceTotalLossTuple;
import com.bank.domain.entity.StockTransaction;
import lombok.Builder;

/**
 * Class to calculate all the operation needed in the Stock market.
 */
@Builder
public class StockOperationCalculator {

    private final StockQuantitySpecification stockQuantitySpecification;

    private final OperationShouldPayTaxesSpecification operationShouldPayTaxesSpecification;

    /**
     * The weighted-average price is the average buying price taking into account the amount of stocks purchased so far.
     * So when you buy stocks you should recalculate the weighted average price using this formula
     * new-weighted-average-price = ((current-stock-quantity * weighted-average-price) +(new-stock-quantity * new-price)) / (current-stock-quantity + new-stock-quantity)
     * e.g., if you have bought 10 stocks for $ 20.00, sold 5 and bought other 5 for $ 10.00, the weighted average is
     * ((5 x20.00) + (5 x 10.00)) / (5 + 5) = 15.00
     *
     * @param currentStockQuantity currentStockQuantity
     * @param weightedAveragePrice weightedAveragePrice
     * @param newStockQuantity     newStockQuantity
     * @param newStockPrice        newStockPrice
     * @return the new weighted average price
     */
    public Double calculateWeighedAverage(Long currentStockQuantity, Double weightedAveragePrice, Long newStockQuantity, Double newStockPrice) {
        if (!stockQuantitySpecification.isSatisfiedBy(currentStockQuantity) && !stockQuantitySpecification.isSatisfiedBy(newStockQuantity)) {
            throw new IllegalArgumentException("Current stock quantity plus new stock quantity should be grater than zero");
        }
        return ((currentStockQuantity * weightedAveragePrice) + (newStockQuantity * newStockPrice)) / (currentStockQuantity + newStockQuantity);
    }

    /**
     * The performance in a stock operation is the money that an operation whether loss or profit on a transaction
     * it is calculated taking into account the weighted-average price and following the formula:
     * performance = operation-stock-quantity (operation-unit-cost * weighted-average-price)
     * e.g., if you have bought 10 stocks for $ 20.00, sold 5 for $10.00, the performance is
     * 5 * (10.00 - 20.00) = -50 which means the Performance type was a loss, loosing $50.00
     *
     * @param weightedAveragePrice
     * @return
     */
    public OperationPerformance calculatePerformance(StockOperation stockOperation, Double weightedAveragePrice) {
        Double performance = 0d;
        if (StockOperation.isSell().test(stockOperation)) {
            performance = stockOperation.getQuantity() * (stockOperation.getUnitCost() - weightedAveragePrice);
        }
        return OperationPerformance.builder().amount(performance)
                .performanceType(OperationPerformanceType.getOperationPerformanceTypeByPerformance(performance))
                .build();

    }

    /**
     * Calculates the new total loss
     *
     * @param stockTransaction
     * @param operationPerformance
     * @return
     */
    public OperationPerformanceTotalLossTuple calculateNewTotalLoss(StockTransaction stockTransaction, OperationPerformance operationPerformance, StockOperation stockOperation) {
        Double totalLoss = stockTransaction.getTotalLoss();
        Double newTotalLoss = stockTransaction.getTotalLoss();
        if (OperationPerformance.isLoss().test(operationPerformance)) {
            newTotalLoss = stockTransaction.getTotalLoss() + operationPerformance.getAmount();
        } else if (OperationPerformance.isProfit().test(operationPerformance) && totalLoss < 0 && operationShouldPayTaxesSpecification.isSatisfiedBy(stockOperation)) {
            newTotalLoss = totalLoss + operationPerformance.getAmount() < 0 ? totalLoss + operationPerformance.getAmount() : 0;
            operationPerformance = calculatePerformanceAfterDeductLoss(operationPerformance, totalLoss);
        }
        return OperationPerformanceTotalLossTuple.builder().operationPerformance(operationPerformance).totalLoss(newTotalLoss).build();
    }

    private OperationPerformance calculatePerformanceAfterDeductLoss(OperationPerformance operationPerformance, Double newTotalLoss) {
        Double newPerformance = operationPerformance.getAmount() + newTotalLoss > 0 ? operationPerformance.getAmount() + newTotalLoss: 0;
        operationPerformance = OperationPerformance.builder().amount(newPerformance).performanceType(OperationPerformanceType.getOperationPerformanceTypeByPerformance(newPerformance)).build();
        return operationPerformance;
    }

    /**
     * Calculate the new stock quantity given an operation
     *
     * @param stockOperation       Current operation performed
     * @param currentStockQuantity current stock quantity
     * @return
     */
    public Long calculateNewStockQuantity(StockOperation stockOperation, Long currentStockQuantity) {
        Long newStockQuantity = currentStockQuantity;
        if (StockOperation.isSell().test(stockOperation)) {
            newStockQuantity = currentStockQuantity - stockOperation.getQuantity();
        } else if (StockOperation.isBuy().test(stockOperation)) {
            newStockQuantity = currentStockQuantity + stockOperation.getQuantity();
        }
        return newStockQuantity;
    }

    /**
     * Calculate the new performance if there are a historical loss
     *
     * @param operationPerformance Performance of the current operation
     * @param totalLoss            the loss total of the transaction
     * @return
     */
    public OperationPerformance calculatePerformanceWithTheNewTotalLoss(OperationPerformance operationPerformance, Double totalLoss) {
        if (OperationPerformance.isProfit().test(operationPerformance) && totalLoss < 0) {
            operationPerformance = OperationPerformance.builder().performanceType(operationPerformance.getPerformanceType()).amount(operationPerformance.getAmount() + totalLoss).build();
        }
        return operationPerformance;
    }
}
