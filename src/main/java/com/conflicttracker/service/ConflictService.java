package com.conflicttracker.service;

import com.conflicttracker.dto.ConflictDTO;
import com.conflicttracker.dto.ConflictSummaryDTO;
import com.conflicttracker.exception.ResourceNotFoundException;
import com.conflicttracker.mapper.ConflictMapper;
import com.conflicttracker.model.Conflict;
import com.conflicttracker.model.ConflictStatus;
import com.conflicttracker.model.Country;
import com.conflicttracker.repository.ConflictRepository;
import com.conflicttracker.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConflictService {
    
    private final ConflictRepository conflictRepository;
    private final CountryRepository countryRepository;
    private final ConflictMapper conflictMapper;
    
    @Transactional(readOnly = true)
    public List<ConflictSummaryDTO> getAllConflicts() {
        return conflictRepository.findAll().stream()
                .map(conflictMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ConflictDTO> getAllConflictsDTO() {
        return conflictRepository.findAll().stream()
                .map(conflictMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ConflictSummaryDTO> getConflictsByStatus(ConflictStatus status) {
        return conflictRepository.findByStatus(status).stream()
                .map(conflictMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ConflictDTO getConflictById(Long id) {
        Conflict conflict = conflictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + id));
        return conflictMapper.toDTO(conflict);
    }
    
    public ConflictDTO createConflict(ConflictDTO conflictDTO) {
        Conflict conflict = conflictMapper.toEntity(conflictDTO);
        
        // Assign existing countries
        if (conflictDTO.getCountries() != null && !conflictDTO.getCountries().isEmpty()) {
            Set<Country> countries = new HashSet<>();
            conflictDTO.getCountries().forEach(countryDTO -> {
                Country country = countryRepository.findById(countryDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryDTO.getId()));
                countries.add(country);
            });
            conflict.setCountries(countries);
        }
        
        Conflict savedConflict = conflictRepository.save(conflict);
        return conflictMapper.toDTO(savedConflict);
    }
    
    public ConflictDTO updateConflict(Long id, ConflictDTO conflictDTO) {
        Conflict existingConflict = conflictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + id));
        
        existingConflict.setName(conflictDTO.getName());
        existingConflict.setStartDate(conflictDTO.getStartDate());
        existingConflict.setStatus(conflictDTO.getStatus());
        existingConflict.setDescription(conflictDTO.getDescription());
        
        // Update countries
        if (conflictDTO.getCountries() != null) {
            Set<Country> countries = new HashSet<>();
            conflictDTO.getCountries().forEach(countryDTO -> {
                Country country = countryRepository.findById(countryDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryDTO.getId()));
                countries.add(country);
            });
            existingConflict.setCountries(countries);
        }
        
        Conflict updatedConflict = conflictRepository.save(existingConflict);
        return conflictMapper.toDTO(updatedConflict);
    }
    
    public void deleteConflict(Long id) {
        if (!conflictRepository.existsById(id)) {
            throw new ResourceNotFoundException("Conflict not found with id: " + id);
        }
        conflictRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<ConflictSummaryDTO> getConflictsByCountryCode(String countryCode) {
        return conflictRepository.findByCountryCode(countryCode).stream()
                .map(conflictMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }
}
