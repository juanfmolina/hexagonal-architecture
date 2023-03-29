package com.bank.domain.valueobject;

public enum OperationPerformanceType {
    LOSS, PROFIT;

    public static OperationPerformanceType getOperationPerformanceTypeByPerformance(Double performance){
        if (performance > 0){
            return PROFIT;
        }
        return LOSS;
    }
}
