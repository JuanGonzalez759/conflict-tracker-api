package com.conflicttracker.repository;

import com.conflicttracker.model.Faction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactionRepository extends JpaRepository<Faction, Long> {
    List<Faction> findByConflictId(Long conflictId);
}
