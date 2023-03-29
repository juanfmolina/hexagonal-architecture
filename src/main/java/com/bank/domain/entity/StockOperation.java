package com.bank.domain.entity;

import com.bank.domain.valueobject.OperationType;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Predicate;

/**
 * Representation of an operation inside the Stock Market.
 */
@Builder
@Getter
public class StockOperation {

    /**
     * Type of performed operation
     */
    private OperationType operationType;

    /**
     * The stock's unit cost using a currency with two decimal places
     */
    private Double unitCost;

    /**
     * The quantity of stocks negotiated
     */
    private Long quantity;

    public static Predicate <StockOperation> isSell(){
        return p -> p.getOperationType() == OperationType.SELL;
    }

    public static Predicate <StockOperation> isBuy(){
        return p -> p.getOperationType() == OperationType.BUY;
    }

}
