package com.bank.domain.valueobject;

import com.bank.domain.entity.StockOperation;
import lombok.Builder;
import lombok.Value;

/**
 * Tuple to represent an Operation and the tax should be paid for that operation
 */
@Value
@Builder
public class OperationStatus extends AbstractEntityStatus {

    private final StockOperation stockOperation;
    private final Tax tax;
}
