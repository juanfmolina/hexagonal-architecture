package com.bank.domain.valueobject;

import lombok.Builder;

@Builder
public class ErrorStatus extends AbstractEntityStatus{

    private String error;
    private ErrorStatus() {
        status = StatusType.ERROR;
    }
}
