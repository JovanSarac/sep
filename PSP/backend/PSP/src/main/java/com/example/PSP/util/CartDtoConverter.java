package com.example.PSP.util;

import com.example.PSP.dtos.CartDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CartDtoConverter implements AttributeConverter<CartDto, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(CartDto cartDto) {
        try {
            return objectMapper.writeValueAsString(cartDto);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting CartDto to JSON", e);
        }
    }

    @Override
    public CartDto convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, CartDto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to CartDto", e);
        }
    }
}
