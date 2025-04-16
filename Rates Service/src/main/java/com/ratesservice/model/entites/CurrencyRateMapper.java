package com.ratesservice.model.entites;

import com.ratesservice.model.dto.CurrencyRateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    @Mapping(source = "code", target = "code")
    @Mapping(source = "buyPrice", target = "buyPrice")
    @Mapping(source = "sellPrice", target = "sellPrice")
    CurrencyRate toEntity(CurrencyRateDto dto);
}