package com.bank.application.usecases;

import com.bank.framework.adapter.output.domain.StockOperationDTO;
import com.bank.framework.adapter.output.domain.TaxDTO;

import java.util.List;

public interface TaxCalculationUseCase {

    /**
     * Calculates how much tax you should pay based on the profit or losses of a stock market investment
     * @param stockOperationList
     * @return
     */
    List<TaxDTO> calculateTaxes(List<StockOperationDTO> stockOperationList);
}
