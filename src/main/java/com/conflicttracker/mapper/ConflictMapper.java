package com.conflicttracker.mapper;

import com.conflicttracker.dto.ConflictDTO;
import com.conflicttracker.dto.ConflictSummaryDTO;
import com.conflicttracker.model.Conflict;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConflictMapper {
    
    private final CountryMapper countryMapper;
    
    public ConflictDTO toDTO(Conflict conflict) {
        if (conflict == null) {
            return null;
        }
        return ConflictDTO.builder()
                .id(conflict.getId())
                .name(conflict.getName())
                .startDate(conflict.getStartDate())
                .status(conflict.getStatus())
                .description(conflict.getDescription())
                .countries(conflict.getCountries().stream()
                        .map(countryMapper::toDTO)
                        .collect(Collectors.toSet()))
                .build();
    }
    
    public ConflictSummaryDTO toSummaryDTO(Conflict conflict) {
        if (conflict == null) {
            return null;
        }
        return ConflictSummaryDTO.builder()
                .id(conflict.getId())
                .name(conflict.getName())
                .startDate(conflict.getStartDate())
                .status(conflict.getStatus())
                .build();
    }
    
    public Conflict toEntity(ConflictDTO dto) {
        if (dto == null) {
            return null;
        }
        Conflict conflict = Conflict.builder()
                .id(dto.getId())
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .build();
        
        if (dto.getCountries() != null) {
            conflict.setCountries(dto.getCountries().stream()
                    .map(countryMapper::toEntity)
                    .collect(Collectors.toSet()));
        }
        
        return conflict;
    }
}
