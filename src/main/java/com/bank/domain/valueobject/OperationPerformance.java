package com.bank.domain.valueobject;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Predicate;

/**
 * Represents the performance of an operation made in the stock market
 */

@Builder
@Getter
public class OperationPerformance {

    private OperationPerformanceType performanceType;

    private Double amount;


    public static Predicate <OperationPerformance> isProfit(){
        return p -> p.getPerformanceType() == OperationPerformanceType.PROFIT;
    }

    public static Predicate <OperationPerformance> isLoss(){
        return p -> p.getPerformanceType() == OperationPerformanceType.LOSS;
    }
}
