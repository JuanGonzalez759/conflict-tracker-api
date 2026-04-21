package com.conflicttracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private Long id;
    
    @NotNull(message = "Event date is required")
    private LocalDate eventDate;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private String description;
    
    @NotNull(message = "Conflict ID is required")
    private Long conflictId;
    
    private String conflictName;
}
