package com.conflicttracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "factions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Faction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conflict_id", nullable = false)
    private Conflict conflict;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "faction_countries",
        joinColumns = @JoinColumn(name = "faction_id"),
        inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    @Builder.Default
    private Set<Country> supportingCountries = new HashSet<>();
}
