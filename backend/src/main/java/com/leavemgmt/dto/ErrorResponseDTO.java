package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErrorResponseDTO - Data Transfer Object for Error Responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private String error;
    private Integer status;
    private Long timestamp;
    private String path;
}
