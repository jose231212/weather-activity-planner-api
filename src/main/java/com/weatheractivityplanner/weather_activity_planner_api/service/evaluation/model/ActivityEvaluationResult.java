package com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ActivityEvaluationResult {

    private ActivityDecision decision;

    private List<String> reasons;

    private String indoorAlternative;

    private LocalDateTime evaluatedAt;

}
