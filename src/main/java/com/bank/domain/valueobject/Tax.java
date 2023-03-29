package com.bank.domain.valueobject;

import lombok.Builder;
import lombok.Getter;

/**
 * The amount of taxes to be paid based on operation
 */
@Getter
@Builder
public class Tax{

    /**
     * Amount of taxes to be paid
     */
    private Double tax;

}
