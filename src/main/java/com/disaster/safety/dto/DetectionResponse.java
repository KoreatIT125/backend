package com.disaster.safety.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetectionResponse {
    private String status;
    private String message;
    private String cctvType;
    private List<Alert> alerts;
    
    @Data
    public static class Alert {
        private String type;
        private String severity;
        private String description;
        private Double confidence;
    }
}
