package com.conflicttracker.controller;

import com.conflicttracker.dto.FactionDTO;
import com.conflicttracker.service.FactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/factions")
@RequiredArgsConstructor
public class FactionController {
    
    private final FactionService factionService;
    
    @GetMapping
    public ResponseEntity<List<FactionDTO>> getAllFactions() {
        List<FactionDTO> factions = factionService.getAllFactions();
        return ResponseEntity.ok(factions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FactionDTO> getFactionById(@PathVariable Long id) {
        FactionDTO faction = factionService.getFactionById(id);
        return ResponseEntity.ok(faction);
    }
    
    @PostMapping
    public ResponseEntity<FactionDTO> createFaction(@Valid @RequestBody FactionDTO factionDTO) {
        FactionDTO createdFaction = factionService.createFaction(factionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaction);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FactionDTO> updateFaction(
            @PathVariable Long id,
            @Valid @RequestBody FactionDTO factionDTO) {
        FactionDTO updatedFaction = factionService.updateFaction(id, factionDTO);
        return ResponseEntity.ok(updatedFaction);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaction(@PathVariable Long id) {
        factionService.deleteFaction(id);
        return ResponseEntity.noContent().build();
    }
}
