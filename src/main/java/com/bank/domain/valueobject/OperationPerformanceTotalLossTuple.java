package com.bank.domain.valueobject;

import lombok.Builder;
import lombok.Value;

/**
 * Tuple to represent an Operation Performance and the new total loss for that operation
 */
@Value
@Builder
public class OperationPerformanceTotalLossTuple {

    private OperationPerformance operationPerformance;
    private Double totalLoss;
}
