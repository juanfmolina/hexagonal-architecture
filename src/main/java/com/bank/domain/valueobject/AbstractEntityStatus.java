package com.bank.domain.valueobject;

public abstract class AbstractEntityStatus implements EntityStatus {

    protected StatusType status;
    @Override
    public StatusType getEntityStatus(){
        return status;
    }

}
