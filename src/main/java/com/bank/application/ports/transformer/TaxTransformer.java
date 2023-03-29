package com.bank.application.ports.transformer;

import com.bank.domain.valueobject.Tax;
import com.bank.framework.adapter.output.domain.TaxDTO;

public class TaxTransformer {
    public static TaxDTO transform(Tax tax) {
        return TaxDTO.builder().tax(tax.getTax()).build();
    }
}
