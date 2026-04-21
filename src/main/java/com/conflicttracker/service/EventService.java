package com.conflicttracker.service;

import com.conflicttracker.dto.EventDTO;
import com.conflicttracker.exception.ResourceNotFoundException;
import com.conflicttracker.mapper.EventMapper;
import com.conflicttracker.model.Conflict;
import com.conflicttracker.model.Event;
import com.conflicttracker.repository.ConflictRepository;
import com.conflicttracker.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    
    private final EventRepository eventRepository;
    private final ConflictRepository conflictRepository;
    private final EventMapper eventMapper;
    
    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return eventMapper.toDTO(event);
    }
    
    @Transactional(readOnly = true)
    public List<EventDTO> getEventsByConflictId(Long conflictId) {
        return eventRepository.findByConflictId(conflictId).stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public EventDTO createEvent(EventDTO eventDTO) {
        Conflict conflict = conflictRepository.findById(eventDTO.getConflictId())
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + eventDTO.getConflictId()));
        
        Event event = Event.builder()
                .eventDate(eventDTO.getEventDate())
                .location(eventDTO.getLocation())
                .description(eventDTO.getDescription())
                .conflict(conflict)
                .build();
        
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDTO(savedEvent);
    }
    
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        
        existingEvent.setEventDate(eventDTO.getEventDate());
        existingEvent.setLocation(eventDTO.getLocation());
        existingEvent.setDescription(eventDTO.getDescription());
        
        // Update conflict if changed
        if (eventDTO.getConflictId() != null && !eventDTO.getConflictId().equals(existingEvent.getConflict().getId())) {
            Conflict conflict = conflictRepository.findById(eventDTO.getConflictId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + eventDTO.getConflictId()));
            existingEvent.setConflict(conflict);
        }
        
        Event updatedEvent = eventRepository.save(existingEvent);
        return eventMapper.toDTO(updatedEvent);
    }
    
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
