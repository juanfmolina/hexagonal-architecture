package com.bank.domain.specification;

import com.bank.domain.specification.common.AbstractSpecification;
import com.bank.domain.valueobject.OperationPerformance;
import lombok.Builder;

@Builder
public class PerformanceShouldPayTaxSpecification extends AbstractSpecification<OperationPerformance> {
    @Override
    public boolean isSatisfiedBy(OperationPerformance performance) {
        return OperationPerformance.isProfit().test(performance);
    }
}
