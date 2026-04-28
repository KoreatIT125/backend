package com.disaster.safety.petmediscan.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.*;

@Service
public class ImageService {
    private static final int TARGET_SIZE = 1280;
    private static final String EYES_FASTAPI_URL = "http://localhost:5000"; // FastAPI 주소
    private static final String SKIN_FASTAPI_URL = "http://localhost:5001"; // FastAPI 주소
    private final RestTemplate restTemplate;

    public ImageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public byte[] resizeImage(MultipartFile file) throws IOException {
        validateImageFile(file);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(file.getInputStream())
                .size(TARGET_SIZE, TARGET_SIZE)
                .keepAspectRatio(false)   // 정확히 1280x1280 (비율 무시)
                .outputFormat("jpg")
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }

    public Map<String, Object> sendToFastApi(byte[] resizedImage, String originalFilename) {
        // multipart/form-data 형태로 FastAPI에 전송
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(resizedImage) {
            @Override
            public String getFilename() {
                return originalFilename != null ? originalFilename : "image.jpg";
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                EYES_FASTAPI_URL,
                requestEntity,
                Map.class
        );

        return response.getBody();
    }

}   
