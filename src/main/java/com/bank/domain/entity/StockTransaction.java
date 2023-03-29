package com.bank.domain.entity;

import com.bank.domain.valueobject.AbstractEntityStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stock Transaction is a set of Stock operations
 * This class is immutable
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StockTransaction {

    /**
     * The current stock quantity
     */

    private Long currentStockQuantity = 0l;

    /**
     * Double that represent the weighted-average price, in a transaction with no operations, the value is 0
     */
    private Double weightedAveragePrice = 0d;

    /**
     * The total loss of this transaction
     */
    private Double totalLoss = 0d;

    /**
     * The list of aggregated operations
     */
    private List<AbstractEntityStatus> operationList;


    /**
     * Create a new reference of a transaction based on a pre-existing transaction.
     *
     * @param operationStatus       Is a representation of an operation made in the market to be added to a transaction
     * @param newWeightedAveragePrice is the new weighted-average price for this transaction
     * @return a representation of this transaction
     */
    public StockTransaction addOperation(AbstractEntityStatus operationStatus, Double newWeightedAveragePrice, Double totalLoss, Long currentStockQuantity){

        if(Objects.isNull(operationStatus)){
            throw new IllegalArgumentException("A non-null Stock operation is needed to add");
        }

        ArrayList<AbstractEntityStatus> newOperationList = new ArrayList<>();
        if(!Objects.isNull(operationList)){
            newOperationList.addAll(operationList);
        }
        newOperationList.add(operationStatus);

        return StockTransaction.builder().operationList(newOperationList).weightedAveragePrice(newWeightedAveragePrice)
                .totalLoss(totalLoss).currentStockQuantity(currentStockQuantity).build();
    }
}
