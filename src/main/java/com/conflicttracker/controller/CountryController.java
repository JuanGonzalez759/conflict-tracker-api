package com.conflicttracker.controller;

import com.conflicttracker.dto.ConflictSummaryDTO;
import com.conflicttracker.service.ConflictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {
    
    private final ConflictService conflictService;
    
    @GetMapping("/{code}/conflicts")
    public ResponseEntity<List<ConflictSummaryDTO>> getConflictsByCountryCode(@PathVariable String code) {
        List<ConflictSummaryDTO> conflicts = conflictService.getConflictsByCountryCode(code);
        return ResponseEntity.ok(conflicts);
    }
}
