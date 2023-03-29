package com.bank.domain.policy;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.specification.OperationShouldPayTaxesSpecification;
import com.bank.domain.specification.PerformanceShouldPayTaxSpecification;
import com.bank.domain.valueobject.OperationPerformance;
import com.bank.domain.valueobject.Tax;
import lombok.Builder;

import java.util.Objects;

/**
 * Policy clas to calculate all related to Taxes
 */
@Builder
public class TaxesCalculator {

    private static final Double PERCENTAGE_TAX_TO_BE_APPLIED = 20d;
    private PerformanceShouldPayTaxSpecification performanceShouldPayTaxSpecification;
    private OperationShouldPayTaxesSpecification operationShouldPayTaxesSpecification;

    /**
     * Calculate tax given a performance
     *
     * @param performance Performance of an operation
     * @return the amount of tax to be paid
     */
    public Tax calculateTaxToPay(OperationPerformance performance, StockOperation stockOperation) {
        Double taxToPay = !Objects.isNull(performance) && !Objects.isNull(stockOperation)
                && performanceShouldPayTaxSpecification.isSatisfiedBy(performance)
                && operationShouldPayTaxesSpecification.isSatisfiedBy(stockOperation)
                ? performance.getAmount() * (PERCENTAGE_TAX_TO_BE_APPLIED / 100) : 0d;
        return Tax.builder().tax(taxToPay).build();
    }

}
