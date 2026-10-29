package com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class WeatherThresholds {

    private double minTemperature;

    private double maxTemperature;

    private int maxPrecipitationProbability;

    private double maxWindSpeed; 

}
