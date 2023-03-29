package com.bank.framework.adapter.output.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaxDTO {
    private Double tax;
}
