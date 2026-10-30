package com.weatheractivityplanner.weather_activity_planner_api.dto.event;

import java.time.LocalDateTime;

import com.weatheractivityplanner.weather_activity_planner_api.entity.enums.ActivityType;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class EventRequestDTO {

    @NotBlank 
    private String name;

    @NotNull
    private ActivityType activityType;

    @NotBlank 
    private String city;

    @NotBlank 
    private String country;

    @NotNull 
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    @NotNull
    private LocalDateTime scheduledAt;

    @NotNull
    private Double minTemperature;

    @NotNull
    private Double maxTemperature;

    @NotNull 
    @Min(0)
    @Max(100)
    private Integer maxPrecipitationProbability;

    @NotNull 
    @DecimalMin("0.0")
    private Double maxWindSpeed;

    private boolean reschedulable;

}
