package com.weatheractivityplanner.weather_activity_planner_api.dto.event;

import java.time.LocalDateTime;

import com.weatheractivityplanner.weather_activity_planner_api.entity.enums.ActivityType;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class EventResponseDTO {

    private Long id;

    private String name;

    private ActivityType activityType;

    private String city;

    private String country;

    private Double latitude;

    private Double longitude;

    private LocalDateTime scheduledAt;

    private Double minTemperature;

    private Double maxTemperature;

    private Integer maxPrecipitationProbability;

    private Double maxWindSpeed;

    private boolean reschedulable;

}
