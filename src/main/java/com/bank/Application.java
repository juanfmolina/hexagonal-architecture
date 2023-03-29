package com.bank;

import com.bank.framework.adapter.input.TaxCalculationViewCLIAdapter;
import com.bank.config.ApplicationConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        String response = "";

        List<String> operationStringList = new ArrayList<>();

        Scanner stringScanner = new Scanner(System.in);

        response = stringScanner.nextLine();
        while (!response.equals(" ")){
            operationStringList.add(response);
            response = stringScanner.nextLine();
        }

        TaxCalculationViewCLIAdapter taxCalculationViewCLIAdapter = ApplicationConfiguration.builder().build().configureDependencies();

        operationStringList.forEach(operationString -> System.out.println(taxCalculationViewCLIAdapter.calculateTaxes(operationString)));
    }
}
