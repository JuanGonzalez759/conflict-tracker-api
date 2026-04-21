package com.conflicttracker.dto;

import com.conflicttracker.model.ConflictStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "Status is required")
    private ConflictStatus status;
    
    private String description;
    
    @Builder.Default
    private Set<CountryDTO> countries = new HashSet<>();
}
