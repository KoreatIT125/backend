package com.disaster.safety.dto;

import lombok.Data;

@Data
public class DetectionRequest {
    private String cctvType;
    private String imageBase64;
}
