package com.disaster.safety.controller;

import com.disaster.safety.dto.DetectionRequest;
import com.disaster.safety.dto.DetectionResponse;
import com.disaster.safety.service.DetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/detection")
@RequiredArgsConstructor
public class DetectionController {

    private final DetectionService detectionService;

    @PostMapping("/analyze")
    public ResponseEntity<DetectionResponse> analyzeImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("cctvType") String cctvType) {
        
        DetectionResponse response = detectionService.analyze(image, cctvType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory() {
        // TODO: 탐지 이력 조회
        return ResponseEntity.ok("Detection history");
    }
}
