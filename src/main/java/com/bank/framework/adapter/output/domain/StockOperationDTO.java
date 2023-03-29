package com.bank.framework.adapter.output.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockOperationDTO {
    /**
     * If the operation was a buy or sell
     */
    private String operation;

    /**
     * The stock's unit cost using a currency with two decimal places
     */
    @JsonProperty("unit-cost")
    private Double unitCost;

    /**
     * The quantity of stocks negotiated
     */
    private Long quantity;

}
