package com.bank.framework.adapter.input;

import com.bank.framework.adapter.output.StringToStockOperationParserAdapter;
import com.bank.framework.adapter.output.StringToTaxListParserAdapter;
import com.bank.framework.adapter.output.domain.StockOperationDTO;
import com.bank.framework.adapter.output.domain.TaxDTO;
import com.bank.application.usecases.TaxCalculationUseCase;
import lombok.Builder;

import java.util.List;

@Builder
public class TaxCalculationViewCLIAdapter {

    private TaxCalculationUseCase taxCalculationUseCase;
    private StringToStockOperationParserAdapter stringToStockOperationParserAdapter;
    private StringToTaxListParserAdapter stringToTaxListParserAdapter;

    public String calculateTaxes(String operationStringList) {
        List<StockOperationDTO> operationList = stringToStockOperationParserAdapter.parseString(operationStringList);
        List<TaxDTO> taxList = taxCalculationUseCase.calculateTaxes(operationList);
        return stringToTaxListParserAdapter.parseObject(taxList);
    }


}
