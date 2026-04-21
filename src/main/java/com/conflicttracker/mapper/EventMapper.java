package com.conflicttracker.mapper;

import com.conflicttracker.dto.EventDTO;
import com.conflicttracker.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    
    public EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }
        return EventDTO.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .description(event.getDescription())
                .conflictId(event.getConflict() != null ? event.getConflict().getId() : null)
                .conflictName(event.getConflict() != null ? event.getConflict().getName() : null)
                .build();
    }
}
