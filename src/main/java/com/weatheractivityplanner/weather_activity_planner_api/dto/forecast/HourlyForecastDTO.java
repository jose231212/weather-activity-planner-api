package com.weatheractivityplanner.weather_activity_planner_api.dto.forecast;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class HourlyForecastDTO {

    private List<LocalDateTime> time;

    private List<Double> temperature_2m;

    private List<Integer> precipitation_probability;

    private List<Double> wind_speed_10m;

    private List<Integer> weather_code;

}
