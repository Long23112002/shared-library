package com.eps.shared.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String traceId;
    private LocalDateTime timestamp;
    private int status;
    private String messageCode;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorDetail> errors;

    // Constructors, Getters, Setters

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String message;
        private Object rejectedValue;

        // Constructors, Getters, Setters
    }
}
