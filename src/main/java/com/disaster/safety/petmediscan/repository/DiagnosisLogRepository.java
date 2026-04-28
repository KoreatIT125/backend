package com.disaster.safety.petmediscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disaster.safety.petmediscan.entity.DiagnosisLog;

public interface DiagnosisLogRepository  extends JpaRepository<DiagnosisLog, Long> {
    
}
