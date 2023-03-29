package com.bank.framework.adapter.output;

import com.bank.framework.adapter.output.domain.StockOperationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.util.List;

/**
 * Class to parse Json representation to Stock operation and otherwise.
 */
@Builder
public class StringToStockOperationParserAdapter implements StringToObjectParserOutputPort <List<StockOperationDTO>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param operationString
     * @return
     */
    @Override
    public List<StockOperationDTO> parseString(String operationString) {
        try {
            return objectMapper.readValue(operationString, objectMapper.getTypeFactory().constructCollectionType(List.class, StockOperationDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param stockOperationDTOList
     * @return
     */
    @Override
    public String parseObject(List<StockOperationDTO> stockOperationDTOList) {
        try {
            return objectMapper.writeValueAsString(stockOperationDTOList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
