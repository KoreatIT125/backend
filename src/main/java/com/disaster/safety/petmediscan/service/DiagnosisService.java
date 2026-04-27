package com.disaster.safety.petmediscan.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.disaster.safety.petmediscan.entity.Diagnosis;
import com.disaster.safety.petmediscan.entity.Pet;
import com.disaster.safety.petmediscan.entity.Types;
import com.disaster.safety.petmediscan.repository.DiagnosisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    
    //처음 이미지 받을때 이미지 리사이징(1280*1280) 후 저장, 진단 결과는 null로 저장(진단결과는 fastapi에서 반환후 다시 매칭해서 저장)


    //fastapi 반환후 처리
    public DiagnosisResponse processDiagnosisResult(FastApiResponse aiResponse, Diagnosis diagnosis) {
        // AI 응답에서 진단 결과와 신뢰도 추출
        String result = aiResponse.getResult();
        BigDecimal confidence = aiResponse.getConfidence();

        // 진단 엔티티 업데이트
        diagnosis.setResult(result);
        diagnosis.setConfidence(confidence);
        diagnosisRepository.save(diagnosis);

        // 클라이언트에 반환할 응답 객체 생성
        DiagnosisResponse response = new DiagnosisResponse();
        response.setDiagnosisId(diagnosis.getId());
        response.setResult(result);
        response.setConfidence(confidence);

        return response;
    }
    public Diagnosis create(Pet pet, Types type,String imageUrl, String result, BigDecimal confidence){
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPet(pet);
        diagnosis.setType(type);
        diagnosis.setImage_url(imageUrl);
        diagnosisRepository.save(diagnosis);
        diagnosis.setResult(result);
        diagnosis.setConfidence(confidence);
        diagnosis.setDiagnosis_date(LocalDateTime.now());
        
        diagnosisRepository.save(diagnosis);
        return diagnosis;
    }

    public List<Diagnosis> findAllByPet(Pet pet){
        List<Diagnosis> diagnosis = diagnosisRepository.findByPet(pet);
        return diagnosis;
    }
}
