package com.weatheractivityplanner.weather_activity_planner_api.service.evaluation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.HourlyForecastDTO;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityDecision;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityEvaluationRequest;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityEvaluationResult;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.WeatherThresholds;

@Service
public class ActivityEvaluationServiceImpl implements ActivityEvaluationService {

    @Override
    public ActivityEvaluationResult evaluate(ActivityEvaluationRequest request, ForecastResponseDTO forecast) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Evaluation request must not be null");
        }
        if (request.getThresholds() == null) {
            throw new IllegalArgumentException(
                    "Weather thresholds must not be null");
        }

        WeatherThresholds thresholds = request.getThresholds();

        validateForecast(forecast);

        double minTemperature = thresholds.getMinTemperature();

        double maxTemperature = thresholds.getMaxTemperature();

        int maxPrecipitationProbability = thresholds.getMaxPrecipitationProbability();

        double maxWindSpeed = thresholds.getMaxWindSpeed();

        HourlyForecastDTO hourly = forecast.getHourly();

        boolean isCorrectTemperature = true;

        boolean isCorrectPrecipitation = true;

        boolean isCorrectWind = true;

        for (int i = 0; i < hourly.getTemperature_2m().size(); i++) {
            double currentTemperature = hourly.getTemperature_2m().get(i);
            int currentPrecipitation = hourly.getPrecipitation_probability().get(i);
            double currentWind = hourly.getWind_speed_10m().get(i);

            if (currentTemperature < minTemperature) {
                isCorrectTemperature = false;
            } else if (currentTemperature > maxTemperature) {
                isCorrectTemperature = false;
            }

            if (currentPrecipitation > maxPrecipitationProbability) {
                isCorrectPrecipitation = false;
            }

            if (currentWind > maxWindSpeed) {
                isCorrectWind = false;
            }

        }

        boolean correctForecast = isCorrectTemperature
                && isCorrectPrecipitation
                && isCorrectWind;

        ActivityDecision decision;

        if (correctForecast) {
            decision = ActivityDecision.MAINTAIN;
        } else if (request.isReschedulable()) {
            decision = ActivityDecision.RESCHEDULE;
        } else {
            decision = ActivityDecision.CANCEL;
        }

        ActivityEvaluationResult activityEvaluationResult = new ActivityEvaluationResult();

        activityEvaluationResult.setDecision(decision);
        activityEvaluationResult.setEvaluatedAt(LocalDateTime.now());

        List<String> reasons = new ArrayList<>();

        if (!isCorrectTemperature) {
            reasons.add("Temperature is outside the allowed range");
        }

        if (!isCorrectPrecipitation) {
            reasons.add("Precipitation is outside the allowed range");
        }

        if (!isCorrectWind) {
            reasons.add("Wind is outside the allowed range");
        }

        activityEvaluationResult.setReasons(reasons);

        if (decision == ActivityDecision.MAINTAIN) {
            activityEvaluationResult.setIndoorAlternative(null);
        } else {
            activityEvaluationResult.setIndoorAlternative(
                    "Consider moving the activity to an indoor venue");
        }

        return activityEvaluationResult;

    }

    private void validateForecast(ForecastResponseDTO forecast) {

        if (forecast == null) {
            throw new IllegalArgumentException(
                    "Forecast must not be null");
        }

        if (forecast.getHourly() == null) {
            throw new IllegalArgumentException(
                    "Forecast hourly must not be null");
        }

        HourlyForecastDTO hourly = forecast.getHourly();

        if (hourly.getTime() == null || hourly.getTime().isEmpty()) {
            throw new IllegalArgumentException(
                    "Forecast time data must not be null or empty");
        }

        if (hourly.getTemperature_2m() == null || hourly.getTemperature_2m().isEmpty()) {
            throw new IllegalArgumentException(
                    "Forecast temperature data must not be null or empty");
        }

        if (hourly.getPrecipitation_probability() == null || hourly.getPrecipitation_probability().isEmpty()) {
            throw new IllegalArgumentException(
                    "Forecast precipitation probability must not be null or empty");
        }

        if (hourly.getWind_speed_10m() == null || hourly.getWind_speed_10m().isEmpty()) {
            throw new IllegalArgumentException(
                    "Forecast wind Speed must not be null or empty");
        }

        int timeSize = hourly.getTime().size();

        if (timeSize != hourly.getTemperature_2m().size()
                || timeSize != hourly.getPrecipitation_probability().size()
                || timeSize != hourly.getWind_speed_10m().size()) {

            throw new IllegalArgumentException(
                    "Forecast lists must have the same number of elements");

        }

    }

}
