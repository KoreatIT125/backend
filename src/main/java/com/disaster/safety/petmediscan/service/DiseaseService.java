package com.disaster.safety.petmediscan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.disaster.safety.petmediscan.entity.Disease;
import com.disaster.safety.petmediscan.entity.Types;
import com.disaster.safety.petmediscan.repository.DiseaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiseaseService { //DB매칭만 담당(Disease에는 질병명, 설명, 치료법, 위험도 등등이 담겨있음 - 로그가 아님!)
    private final DiseaseRepository diseaseRepository;

    // label 리스트로 disease 조회
    public List<Disease> matchDiseases(List<String> labels, Types type) {
    return diseaseRepository.findByNameInAndCategory(labels, type);
}

    // label 하나로 disease 조회
    public Disease matchDisease(String label) {
        return diseaseRepository.findByName(label)
                .orElseThrow(() -> new RuntimeException("질병 정보 없음: " + label));
    }
}
