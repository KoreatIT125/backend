package com.disaster.safety.petmediscan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disaster.safety.petmediscan.entity.Disease;
import com.disaster.safety.petmediscan.entity.Types;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    List<Disease> findByNameIn(List<String> names);

    Optional<Disease> findByName(String name);
    
    List<Disease> findByNameInAndCategory(List<String> names, Types category);
}