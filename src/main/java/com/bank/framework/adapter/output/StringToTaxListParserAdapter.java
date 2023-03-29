package com.bank.framework.adapter.output;

import com.bank.framework.adapter.output.domain.TaxDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.util.List;

/**
 * Class to parse Json representation to Stock operation and otherwise.
 */
@Builder
public class StringToTaxListParserAdapter implements StringToObjectParserOutputPort <List<TaxDTO>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param operationString String value
     * @return List of Tax objects
     */
    @Override
    public List<TaxDTO> parseString(String operationString) {
        try {
            return objectMapper.readValue(operationString, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param taxList Parse the list to a json string
     * @return
     */
    @Override
    public String parseObject(List<TaxDTO> taxList) {
        try {
            return objectMapper.writeValueAsString(taxList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
