package com.disaster.safety.petmediscan.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.disaster.safety.petmediscan.dto.FastApiResponse;
import com.disaster.safety.petmediscan.entity.Types;

@Service

public class FastApiService { // FastAPI와의 통신을 담당하는 서비스
    private final WebClient eyeWebClient;
    private final WebClient skinWebClient;

    public FastApiService(
            @Qualifier("eyeWebClient") WebClient eyeWebClient,
            @Qualifier("skinWebClient") WebClient skinWebClient) {
        this.eyeWebClient = eyeWebClient;
        this.skinWebClient = skinWebClient;
    }

    public FastApiResponse predict(MultipartFile file, Types type) {
        WebClient client = type == Types.Eye ? eyeWebClient : skinWebClient;

        FastApiResponse response = client.post()
                .uri("/predict")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("image", file.getResource()))
                .retrieve()
                .bodyToMono(FastApiResponse.class)
                .block();

        if (response == null || response.getTop5() == null) {
            throw new RuntimeException("FastAPI 응답 없음");
        }
        return response;
    }
}
