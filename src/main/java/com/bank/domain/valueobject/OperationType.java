package com.bank.domain.valueobject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representing the type of operations can be performed
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum OperationType {
    BUY("buy"), SELL("sell");

    /**
     * Operation's Name
     */
    private final String operationName;

    public static OperationType getOperationTypeByName(String name) {
        if (BUY.getOperationName().equals(name)) {
            return BUY;
        } else if (SELL.getOperationName().equals(name)) {
            return SELL;
        } else {
            throw new IllegalArgumentException("There is not a Operation Type with the name " + name);
        }
    }
}
