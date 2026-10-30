package com.weatheractivityplanner.weather_activity_planner_api.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ErrorResponseDTO {

    private Integer status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

}
