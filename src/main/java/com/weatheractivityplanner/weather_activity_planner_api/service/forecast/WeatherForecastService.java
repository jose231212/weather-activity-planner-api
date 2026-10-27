package com.weatheractivityplanner.weather_activity_planner_api.service.forecast;

import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;

public interface  WeatherForecastService {

    ForecastResponseDTO getForecast(String city, String country, String timezone);

}
