package com.bank.framework.adapter.output;

/**
 * Interfase to
 */
public interface StringToObjectParserOutputPort <T>{

    T parseString(String operationString);

    String parseObject(T t);

}
