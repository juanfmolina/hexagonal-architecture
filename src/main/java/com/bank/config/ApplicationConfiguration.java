package com.bank.config;

import com.bank.application.ports.input.TaxCalculationInputPort;
import com.bank.application.usecases.TaxCalculationUseCase;
import com.bank.domain.policy.StockOperationCalculator;
import com.bank.domain.policy.TaxesCalculator;
import com.bank.domain.service.StockOperationService;
import com.bank.domain.specification.OperationShouldPayTaxesSpecification;
import com.bank.domain.specification.PerformanceShouldPayTaxSpecification;
import com.bank.domain.specification.StockQuantitySpecification;
import com.bank.framework.adapter.input.TaxCalculationViewCLIAdapter;
import com.bank.framework.adapter.output.StringToStockOperationParserAdapter;
import com.bank.framework.adapter.output.StringToTaxListParserAdapter;
import com.bank.domain.specification.OperationShouldRecalculateWeightedAverage;
import lombok.Builder;

/**
 * Class to configure the application and its dependencies
 */
@Builder
public class ApplicationConfiguration {

    public TaxCalculationViewCLIAdapter configureDependencies(){
        PerformanceShouldPayTaxSpecification performanceShouldPayTaxSpecification = PerformanceShouldPayTaxSpecification.builder().build();
        OperationShouldPayTaxesSpecification operationShouldPayTaxesSpecification = OperationShouldPayTaxesSpecification.builder().build();
        OperationShouldRecalculateWeightedAverage operationShouldRecalculateWeightedAverage = OperationShouldRecalculateWeightedAverage.builder().build();
        StockQuantitySpecification stockQuantitySpecification = StockQuantitySpecification.builder().build();

        TaxesCalculator taxesCalculator = TaxesCalculator.builder()
                .operationShouldPayTaxesSpecification(operationShouldPayTaxesSpecification)
                .performanceShouldPayTaxSpecification(performanceShouldPayTaxSpecification)
                .build();

        StockOperationCalculator stockOperationCalculator = StockOperationCalculator.builder()
                .stockQuantitySpecification(stockQuantitySpecification).operationShouldPayTaxesSpecification(operationShouldPayTaxesSpecification).build();

        StockOperationService stockOperationService = StockOperationService.builder()
                .stockOperationCalculator(stockOperationCalculator)
                .taxesCalculator(taxesCalculator)
                .operationShouldRecalculateWeightedAverage(operationShouldRecalculateWeightedAverage)
                .build();
        StringToStockOperationParserAdapter stringToStockOperationParserAdapter = StringToStockOperationParserAdapter.builder().build();
        StringToTaxListParserAdapter stringToTaxListParserAdapter = StringToTaxListParserAdapter.builder().build();
        TaxCalculationUseCase taxCalculationUseCase = TaxCalculationInputPort.builder().stockOperationService(stockOperationService).build();
        TaxCalculationViewCLIAdapter taxCalculationViewCLIAdapter = TaxCalculationViewCLIAdapter.builder()
                .taxCalculationUseCase(taxCalculationUseCase)
                .stringToStockOperationParserAdapter(stringToStockOperationParserAdapter)
                .stringToTaxListParserAdapter(stringToTaxListParserAdapter)
                .build();
        return taxCalculationViewCLIAdapter;

    }
}
