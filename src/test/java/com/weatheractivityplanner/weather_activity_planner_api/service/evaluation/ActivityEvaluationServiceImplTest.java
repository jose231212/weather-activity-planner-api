package com.weatheractivityplanner.weather_activity_planner_api.service.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.HourlyForecastDTO;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityDecision;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityEvaluationRequest;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.ActivityEvaluationResult;
import com.weatheractivityplanner.weather_activity_planner_api.service.evaluation.model.WeatherThresholds;

public class ActivityEvaluationServiceImplTest {

    private ActivityEvaluationServiceImpl activityEvaluationServiceImpl = new ActivityEvaluationServiceImpl();

    private ActivityEvaluationRequest activityEvaluationRequest;

    private WeatherThresholds weatherThresholds;

    private ForecastResponseDTO forecastResponseDTO;

    private HourlyForecastDTO hourlyForecastDTO;

    @Test
    void shouldReturnMaintainWhenAllWeatherConditionsAreFavorable() {

        weatherThresholds = new WeatherThresholds();
        weatherThresholds.setMinTemperature(18.0);
        weatherThresholds.setMaxTemperature(30.0);
        weatherThresholds.setMaxPrecipitationProbability(40);
        weatherThresholds.setMaxWindSpeed(25.0);

        activityEvaluationRequest = new ActivityEvaluationRequest();
        activityEvaluationRequest.setThresholds(weatherThresholds);
        activityEvaluationRequest.setReschedulable(true);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(24.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(10));
        hourlyForecastDTO.setWind_speed_10m(List.of(12.0));

        forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        ActivityEvaluationResult activityEvaluationResult = activityEvaluationServiceImpl
                .evaluate(activityEvaluationRequest, forecastResponseDTO);

        assertEquals(ActivityDecision.MAINTAIN, activityEvaluationResult.getDecision());
        assertTrue(activityEvaluationResult.getReasons().isEmpty());
        assertNull(activityEvaluationResult.getIndoorAlternative());
        assertNotNull(activityEvaluationResult.getEvaluatedAt());

    }

    @Test
    void shouldReturnRescheduleWhenTemperatureIsBelowMinimum() {

        weatherThresholds = new WeatherThresholds();
        weatherThresholds.setMinTemperature(18.0);
        weatherThresholds.setMaxTemperature(30.0);
        weatherThresholds.setMaxPrecipitationProbability(40);
        weatherThresholds.setMaxWindSpeed(25.0);

        activityEvaluationRequest = new ActivityEvaluationRequest();
        activityEvaluationRequest.setThresholds(weatherThresholds);
        activityEvaluationRequest.setReschedulable(true);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(15.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(10));
        hourlyForecastDTO.setWind_speed_10m(List.of(12.0));

        forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        ActivityEvaluationResult activityEvaluationResult = activityEvaluationServiceImpl
                .evaluate(activityEvaluationRequest, forecastResponseDTO);

        assertEquals(ActivityDecision.RESCHEDULE, activityEvaluationResult.getDecision());
        assertTrue(activityEvaluationResult.getReasons().contains("Temperature is outside the allowed range"));
        assertNotNull(activityEvaluationResult.getIndoorAlternative());
        assertNotNull(activityEvaluationResult.getEvaluatedAt());

    }

    @Test
    void shouldReturnRescheduleWhenTemperatureIsAboveMaximum() {

        weatherThresholds = new WeatherThresholds();
        weatherThresholds.setMinTemperature(18.0);
        weatherThresholds.setMaxTemperature(30.0);
        weatherThresholds.setMaxPrecipitationProbability(40);
        weatherThresholds.setMaxWindSpeed(25.0);

        activityEvaluationRequest = new ActivityEvaluationRequest();
        activityEvaluationRequest.setThresholds(weatherThresholds);
        activityEvaluationRequest.setReschedulable(true);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(35.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(10));
        hourlyForecastDTO.setWind_speed_10m(List.of(12.0));

        forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        ActivityEvaluationResult activityEvaluationResult = activityEvaluationServiceImpl
                .evaluate(activityEvaluationRequest, forecastResponseDTO);

        assertEquals(ActivityDecision.RESCHEDULE, activityEvaluationResult.getDecision());
        assertTrue(activityEvaluationResult.getReasons().contains("Temperature is outside the allowed range"));
        assertNotNull(activityEvaluationResult.getIndoorAlternative());
        assertNotNull(activityEvaluationResult.getEvaluatedAt());

    }

    @Test
    void shouldReturnRescheduleWhenPrecipitationExceedsMaximum() {

        weatherThresholds = new WeatherThresholds();
        weatherThresholds.setMinTemperature(18.0);
        weatherThresholds.setMaxTemperature(30.0);
        weatherThresholds.setMaxPrecipitationProbability(40);
        weatherThresholds.setMaxWindSpeed(25.0);

        activityEvaluationRequest = new ActivityEvaluationRequest();
        activityEvaluationRequest.setThresholds(weatherThresholds);
        activityEvaluationRequest.setReschedulable(true);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(24.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(60));
        hourlyForecastDTO.setWind_speed_10m(List.of(12.0));

        forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        ActivityEvaluationResult activityEvaluationResult = activityEvaluationServiceImpl
                .evaluate(activityEvaluationRequest, forecastResponseDTO);

        assertEquals(ActivityDecision.RESCHEDULE, activityEvaluationResult.getDecision());
        assertTrue(activityEvaluationResult.getReasons().contains("Precipitation is outside the allowed range"));
        assertNotNull(activityEvaluationResult.getIndoorAlternative());
        assertNotNull(activityEvaluationResult.getEvaluatedAt());

    }

    @Test
    void shouldReturnRescheduleWhenWindExceedsMaximum() {

        weatherThresholds = new WeatherThresholds();
        weatherThresholds.setMinTemperature(18.0);
        weatherThresholds.setMaxTemperature(30.0);
        weatherThresholds.setMaxPrecipitationProbability(40);
        weatherThresholds.setMaxWindSpeed(25.0);

        activityEvaluationRequest = new ActivityEvaluationRequest();
        activityEvaluationRequest.setThresholds(weatherThresholds);
        activityEvaluationRequest.setReschedulable(true);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(24.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(10));
        hourlyForecastDTO.setWind_speed_10m(List.of(35.0));

        forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        ActivityEvaluationResult activityEvaluationResult = activityEvaluationServiceImpl
                .evaluate(activityEvaluationRequest, forecastResponseDTO);

        assertEquals(ActivityDecision.RESCHEDULE, activityEvaluationResult.getDecision());
        assertTrue(activityEvaluationResult.getReasons().contains("Wind is outside the allowed range"));
        assertNotNull(activityEvaluationResult.getIndoorAlternative());
        assertNotNull(activityEvaluationResult.getEvaluatedAt());

    }

    @Test
    void shouldReturnCancelWhenConditionsAreUnfavorableAndActivityIsNotReschedulable() {

        weatherThresholds = new WeatherThresholds();
        weatherThresholds.setMinTemperature(18.0);
        weatherThresholds.setMaxTemperature(30.0);
        weatherThresholds.setMaxPrecipitationProbability(40);
        weatherThresholds.setMaxWindSpeed(25.0);

        activityEvaluationRequest = new ActivityEvaluationRequest();
        activityEvaluationRequest.setThresholds(weatherThresholds);
        activityEvaluationRequest.setReschedulable(false);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(24.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(10));
        hourlyForecastDTO.setWind_speed_10m(List.of(35.0));

        forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        ActivityEvaluationResult activityEvaluationResult = activityEvaluationServiceImpl
                .evaluate(activityEvaluationRequest, forecastResponseDTO);

        assertEquals(ActivityDecision.CANCEL, activityEvaluationResult.getDecision());
        assertTrue(activityEvaluationResult.getReasons().contains("Wind is outside the allowed range"));
        assertNotNull(activityEvaluationResult.getIndoorAlternative());
        assertNotNull(activityEvaluationResult.getEvaluatedAt());

    }

    @Test
    void shouldReturnAllReasonsWhenMultipleWeatherConditionsAreUnfavorable() {

        weatherThresholds = new WeatherThresholds();
        weatherThresholds.setMinTemperature(18.0);
        weatherThresholds.setMaxTemperature(30.0);
        weatherThresholds.setMaxPrecipitationProbability(40);
        weatherThresholds.setMaxWindSpeed(25.0);

        activityEvaluationRequest = new ActivityEvaluationRequest();
        activityEvaluationRequest.setThresholds(weatherThresholds);
        activityEvaluationRequest.setReschedulable(true);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(35.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(60));
        hourlyForecastDTO.setWind_speed_10m(List.of(35.0));

        forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        ActivityEvaluationResult activityEvaluationResult = activityEvaluationServiceImpl
                .evaluate(activityEvaluationRequest, forecastResponseDTO);

        assertEquals(ActivityDecision.RESCHEDULE, activityEvaluationResult.getDecision());
        assertTrue(activityEvaluationResult.getReasons().contains("Temperature is outside the allowed range"));
        assertTrue(activityEvaluationResult.getReasons().contains("Wind is outside the allowed range"));
        assertTrue(activityEvaluationResult.getReasons().contains("Precipitation is outside the allowed range"));
        assertNotNull(activityEvaluationResult.getIndoorAlternative());
        assertNotNull(activityEvaluationResult.getEvaluatedAt());

    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {

        ForecastResponseDTO forecastResponseDTO = new ForecastResponseDTO();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> activityEvaluationServiceImpl.evaluate(null, forecastResponseDTO));

        assertEquals("Evaluation request must not be null", exception.getMessage());    

    }

    @Test 
    void shouldThrowExceptionWhenThresholdsAreNull() {

        ActivityEvaluationRequest request = new ActivityEvaluationRequest();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> activityEvaluationServiceImpl.evaluate(request, null));

        assertEquals("Weather thresholds must not be null", exception.getMessage());

    }

    @Test 
    void shouldThrowExceptionWhenForecastIsNull() {

        WeatherThresholds thresholds = new WeatherThresholds();
        thresholds.setMinTemperature(18.0);
        thresholds.setMaxTemperature(30.0);
        thresholds.setMaxPrecipitationProbability(40);
        thresholds.setMaxWindSpeed(25.0);

        ActivityEvaluationRequest request = new ActivityEvaluationRequest();
        request.setThresholds(thresholds);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> activityEvaluationServiceImpl.evaluate(request, null));

        assertEquals("Forecast must not be null", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenHourlyForecastIsNull() {

        WeatherThresholds thresholds = new WeatherThresholds();
        thresholds.setMinTemperature(18.0);
        thresholds.setMaxTemperature(30.0);
        thresholds.setMaxPrecipitationProbability(40);
        thresholds.setMaxWindSpeed(25.0);

        ActivityEvaluationRequest request = new ActivityEvaluationRequest();
        request.setThresholds(thresholds);

        ForecastResponseDTO forecastResponseDTO = new ForecastResponseDTO();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> activityEvaluationServiceImpl.evaluate(request, forecastResponseDTO));

        assertEquals("Forecast hourly must not be null", exception.getMessage());


    }

    @Test 
    void shouldThrowExceptionWhenForecastListsHaveDifferentSizes() {

        WeatherThresholds thresholds = new WeatherThresholds();
        thresholds.setMinTemperature(18.0);
        thresholds.setMaxTemperature(30.0);
        thresholds.setMaxPrecipitationProbability(40);
        thresholds.setMaxWindSpeed(25.0);

        ActivityEvaluationRequest request = new ActivityEvaluationRequest();
        request.setThresholds(thresholds);

        hourlyForecastDTO = new HourlyForecastDTO();
        hourlyForecastDTO.setTime(List.of(LocalDateTime.now(), LocalDateTime.now()));
        hourlyForecastDTO.setTemperature_2m(List.of(25.0));
        hourlyForecastDTO.setPrecipitation_probability(List.of(10, 15));
        hourlyForecastDTO.setWind_speed_10m(List.of(25.0, 35.0));

        ForecastResponseDTO forecastResponseDTO = new ForecastResponseDTO();
        forecastResponseDTO.setHourly(hourlyForecastDTO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> activityEvaluationServiceImpl.evaluate(request, forecastResponseDTO));

        assertEquals("Forecast lists must have the same number of elements", exception.getMessage());    


    }   

}
