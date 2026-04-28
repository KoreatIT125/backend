package com.disaster.safety.petmediscan.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.disaster.safety.petmediscan.entity.DiagnosisLog;
import com.disaster.safety.petmediscan.entity.Disease;
import com.disaster.safety.petmediscan.repository.DiagnosisLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosisLogService {
    private final DiagnosisLogRepository diagnosisLogRepository;

    public void saveLogs(List<Disease> diseases, Map<String, Double> scoreMap) {
        List<DiagnosisLog> logs = diseases.stream()
                .map(disease -> {
                    DiagnosisLog log = new DiagnosisLog();
                    log.setDisease(disease);
                    log.setScore(scoreMap.getOrDefault(disease.getName(), 0.0));
                    return log;
                })
                .toList();

        diagnosisLogRepository.saveAll(logs);
    }
}