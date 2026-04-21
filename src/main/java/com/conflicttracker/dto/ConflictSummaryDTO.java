package com.conflicttracker.dto;

import com.conflicttracker.model.ConflictStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictSummaryDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private ConflictStatus status;
}
