package com.conflicttracker.service;

import com.conflicttracker.dto.FactionDTO;
import com.conflicttracker.exception.ResourceNotFoundException;
import com.conflicttracker.mapper.FactionMapper;
import com.conflicttracker.model.Conflict;
import com.conflicttracker.model.Country;
import com.conflicttracker.model.Faction;
import com.conflicttracker.repository.ConflictRepository;
import com.conflicttracker.repository.CountryRepository;
import com.conflicttracker.repository.FactionRepository;
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
public class FactionService {
    
    private final FactionRepository factionRepository;
    private final ConflictRepository conflictRepository;
    private final CountryRepository countryRepository;
    private final FactionMapper factionMapper;
    
    @Transactional(readOnly = true)
    public List<FactionDTO> getAllFactions() {
        return factionRepository.findAll().stream()
                .map(factionMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public FactionDTO getFactionById(Long id) {
        Faction faction = factionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faction not found with id: " + id));
        return factionMapper.toDTO(faction);
    }
    
    @Transactional(readOnly = true)
    public List<FactionDTO> getFactionsByConflictId(Long conflictId) {
        return factionRepository.findByConflictId(conflictId).stream()
                .map(factionMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public FactionDTO createFaction(FactionDTO factionDTO) {
        Conflict conflict = conflictRepository.findById(factionDTO.getConflictId())
                .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + factionDTO.getConflictId()));
        
        Faction faction = Faction.builder()
                .name(factionDTO.getName())
                .conflict(conflict)
                .build();
        
        // Assign supporting countries
        if (factionDTO.getSupportingCountries() != null && !factionDTO.getSupportingCountries().isEmpty()) {
            Set<Country> countries = new HashSet<>();
            factionDTO.getSupportingCountries().forEach(countryDTO -> {
                Country country = countryRepository.findById(countryDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryDTO.getId()));
                countries.add(country);
            });
            faction.setSupportingCountries(countries);
        }
        
        Faction savedFaction = factionRepository.save(faction);
        return factionMapper.toDTO(savedFaction);
    }
    
    public FactionDTO updateFaction(Long id, FactionDTO factionDTO) {
        Faction existingFaction = factionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faction not found with id: " + id));
        
        existingFaction.setName(factionDTO.getName());
        
        // Update conflict if changed
        if (factionDTO.getConflictId() != null && !factionDTO.getConflictId().equals(existingFaction.getConflict().getId())) {
            Conflict conflict = conflictRepository.findById(factionDTO.getConflictId())
                    .orElseThrow(() -> new ResourceNotFoundException("Conflict not found with id: " + factionDTO.getConflictId()));
            existingFaction.setConflict(conflict);
        }
        
        // Update supporting countries
        if (factionDTO.getSupportingCountries() != null) {
            Set<Country> countries = new HashSet<>();
            factionDTO.getSupportingCountries().forEach(countryDTO -> {
                Country country = countryRepository.findById(countryDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + countryDTO.getId()));
                countries.add(country);
            });
            existingFaction.setSupportingCountries(countries);
        }
        
        Faction updatedFaction = factionRepository.save(existingFaction);
        return factionMapper.toDTO(updatedFaction);
    }
    
    public void deleteFaction(Long id) {
        if (!factionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Faction not found with id: " + id);
        }
        factionRepository.deleteById(id);
    }
}
