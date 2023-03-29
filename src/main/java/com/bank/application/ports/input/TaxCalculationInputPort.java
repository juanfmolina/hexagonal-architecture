package com.bank.application.ports.input;

import com.bank.application.ports.transformer.StockOperationTransformer;
import com.bank.application.ports.transformer.TaxTransformer;
import com.bank.domain.entity.StockOperation;
import com.bank.domain.service.StockOperationService;
import com.bank.domain.valueobject.OperationStatus;
import com.bank.framework.adapter.output.domain.StockOperationDTO;
import com.bank.framework.adapter.output.domain.TaxDTO;
import com.bank.application.usecases.TaxCalculationUseCase;
import com.bank.domain.entity.StockTransaction;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Entry points to access of the application
 */
@Builder
public class TaxCalculationInputPort implements TaxCalculationUseCase {

    private StockOperationService stockOperationService;

    /**
     * Calculates how much tax you should pay based on the profit or losses of a stock market investment
     *
     * @param stockOperationDTOList A list of Operation DTO
     * @return the list of TaxDTO
     */
    @Override
    public List<TaxDTO> calculateTaxes(List<StockOperationDTO> stockOperationDTOList) {
        List<StockOperation> stockOperationList = stockOperationDTOList.stream().map(StockOperationTransformer::transform).collect(Collectors.toList());
        StockTransaction stockTransaction = stockOperationService.processOperation(stockOperationList);
        return stockTransaction.getOperationList().stream().map(OperationStatus::getTax).map(TaxTransformer::transform).collect(Collectors.toList());
    }
}
