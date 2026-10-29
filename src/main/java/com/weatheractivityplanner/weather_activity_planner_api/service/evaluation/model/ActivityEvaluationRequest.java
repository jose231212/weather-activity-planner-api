package com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ActivityEvaluationRequest {

    private WeatherThresholds thresholds;

    private boolean reschedulable;

}
