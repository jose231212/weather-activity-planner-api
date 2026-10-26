package com.weatheractivityplanner.weather_activity_planner_api.dto.forecast;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ForecastResponseDTO {

    private double latitude;

    private double longitude;

    private String timezone;

    private HourlyForecastDTO hourly;

}
