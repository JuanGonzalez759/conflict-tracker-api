package com.conflicttracker.controller;

import com.conflicttracker.dto.ConflictDTO;
import com.conflicttracker.dto.ConflictSummaryDTO;
import com.conflicttracker.model.ConflictStatus;
import com.conflicttracker.service.ConflictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conflicts")
@RequiredArgsConstructor
public class ConflictController {
    
    private final ConflictService conflictService;
    
    @GetMapping
    public ResponseEntity<List<ConflictSummaryDTO>> getAllConflicts(
            @RequestParam(required = false) ConflictStatus status) {
        List<ConflictSummaryDTO> conflicts;
        if (status != null) {
            conflicts = conflictService.getConflictsByStatus(status);
        } else {
            conflicts = conflictService.getAllConflicts();
        }
        return ResponseEntity.ok(conflicts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ConflictDTO> getConflictById(@PathVariable Long id) {
        ConflictDTO conflict = conflictService.getConflictById(id);
        return ResponseEntity.ok(conflict);
    }
    
    @PostMapping
    public ResponseEntity<ConflictDTO> createConflict(@Valid @RequestBody ConflictDTO conflictDTO) {
        ConflictDTO createdConflict = conflictService.createConflict(conflictDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConflict);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ConflictDTO> updateConflict(
            @PathVariable Long id,
            @Valid @RequestBody ConflictDTO conflictDTO) {
        ConflictDTO updatedConflict = conflictService.updateConflict(id, conflictDTO);
        return ResponseEntity.ok(updatedConflict);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConflict(@PathVariable Long id) {
        conflictService.deleteConflict(id);
        return ResponseEntity.noContent().build();
    }
}
