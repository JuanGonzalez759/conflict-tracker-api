package com.conflicttracker.mapper;

import com.conflicttracker.dto.FactionDTO;
import com.conflicttracker.model.Faction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FactionMapper {
    
    private final CountryMapper countryMapper;
    
    public FactionDTO toDTO(Faction faction) {
        if (faction == null) {
            return null;
        }
        return FactionDTO.builder()
                .id(faction.getId())
                .name(faction.getName())
                .conflictId(faction.getConflict() != null ? faction.getConflict().getId() : null)
                .conflictName(faction.getConflict() != null ? faction.getConflict().getName() : null)
                .supportingCountries(faction.getSupportingCountries().stream()
                        .map(countryMapper::toDTO)
                        .collect(Collectors.toSet()))
                .build();
    }
}
