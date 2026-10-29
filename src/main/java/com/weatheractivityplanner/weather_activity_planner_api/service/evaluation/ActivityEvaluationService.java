package com.weatheractivityplanner.weather_activity_planner_api.service.evaluation;

import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityEvaluationRequest;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityEvaluationResult;

public interface ActivityEvaluationService {

    ActivityEvaluationResult evaluate(
        ActivityEvaluationRequest request,
        ForecastResponseDTO forecast);

}
