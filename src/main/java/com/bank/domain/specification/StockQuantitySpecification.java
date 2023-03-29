package com.bank.domain.specification;

import com.bank.domain.specification.common.AbstractSpecification;
import lombok.Builder;

/**
 * Specification to validate that any stock quantity is greater than zero
 */
@Builder
public class StockQuantitySpecification extends AbstractSpecification<Long> {
    @Override
    public boolean isSatisfiedBy(Long stockQuantity) {
        return stockQuantity > 0;
    }
}
