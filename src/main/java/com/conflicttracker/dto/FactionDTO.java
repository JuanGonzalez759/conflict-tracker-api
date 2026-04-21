package com.conflicttracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactionDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Conflict ID is required")
    private Long conflictId;
    
    private String conflictName;
    
    @Builder.Default
    private Set<CountryDTO> supportingCountries = new HashSet<>();
}
