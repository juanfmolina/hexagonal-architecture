package com.bank.application.ports.transformer;

import com.bank.domain.entity.StockOperation;
import com.bank.domain.valueobject.OperationType;
import com.bank.framework.adapter.output.domain.StockOperationDTO;

public class StockOperationTransformer {

    public static StockOperation transform(StockOperationDTO stockOperationDTO){
        return StockOperation.builder().unitCost(stockOperationDTO.getUnitCost())
                .quantity(stockOperationDTO.getQuantity())
                .operationType(OperationType.getOperationTypeByName(stockOperationDTO.getOperation()))
                .build();
    }

}
