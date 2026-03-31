package com.disaster.safety.service;

import com.disaster.safety.dto.DetectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DetectionService {

    public DetectionResponse analyze(MultipartFile image, String cctvType) {
        // TODO: AI 모델 호출 로직
        // 1. 이미지 전처리
        // 2. AI 모델 API 호출 (http://ai-model:5000/predict)
        // 3. 결과 파싱 및 반환
        
        DetectionResponse response = new DetectionResponse();
        response.setStatus("success");
        response.setMessage("Image analysis completed");
        response.setCctvType(cctvType);
        
        return response;
    }
}
