package com.conflicttracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "conflicts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conflict {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConflictStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "conflict_countries",
        joinColumns = @JoinColumn(name = "conflict_id"),
        inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    @Builder.Default
    private Set<Country> countries = new HashSet<>();
    
    @OneToMany(mappedBy = "conflict", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Faction> factions = new HashSet<>();
    
    @OneToMany(mappedBy = "conflict", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Event> events = new HashSet<>();
}
