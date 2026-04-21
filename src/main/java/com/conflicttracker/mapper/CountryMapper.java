package com.conflicttracker.mapper;

import com.conflicttracker.dto.CountryDTO;
import com.conflicttracker.model.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {
    
    public CountryDTO toDTO(Country country) {
        if (country == null) {
            return null;
        }
        return CountryDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .code(country.getCode())
                .build();
    }
    
    public Country toEntity(CountryDTO dto) {
        if (dto == null) {
            return null;
        }
        return Country.builder()
                .id(dto.getId())
                .name(dto.getName())
                .code(dto.getCode())
                .build();
    }
}
