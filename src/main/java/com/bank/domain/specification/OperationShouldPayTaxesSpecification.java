package com.bank.domain.specification;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.specification.common.AbstractSpecification;
import lombok.Builder;

/**
 * Specification to determine if an operation should pay taxes
 */
@Builder
public class OperationShouldPayTaxesSpecification extends AbstractSpecification<StockOperation> {
    private static final Double AMOUNT_EXEMPT_TO_PAY_TAX = 20000d;

    @Override
    public boolean isSatisfiedBy(StockOperation stockOperation) {

        return StockOperation.isSell().test(stockOperation) &&
                stockOperation.getQuantity() * stockOperation.getUnitCost() > AMOUNT_EXEMPT_TO_PAY_TAX;
    }
}
