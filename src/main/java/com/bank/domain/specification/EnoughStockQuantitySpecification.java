package com.bank.domain.specification;

import com.bank.domain.specification.common.AbstractSpecification;

public class EnoughStockQuantitySpecification extends AbstractSpecification<Long> {
    /**
     * @param newSockQuantity
     * @return
     */
    @Override
    public boolean isSatisfiedBy(Long newSockQuantity) {
        return newSockQuantity >= 0;
    }
}
