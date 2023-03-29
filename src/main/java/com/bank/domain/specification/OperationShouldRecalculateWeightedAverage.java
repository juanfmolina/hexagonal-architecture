package com.bank.domain.specification;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.specification.common.AbstractSpecification;
import lombok.Builder;

@Builder
public class OperationShouldRecalculateWeightedAverage extends AbstractSpecification<StockOperation> {


    /**
     * Evaluate if the weighted-average price should be recalculated given an operation
     * @param stockOperation
     * @return
     */
    @Override
    public boolean isSatisfiedBy(StockOperation stockOperation) {
        return StockOperation.isBuy().test(stockOperation);
    }
}
