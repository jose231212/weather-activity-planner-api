package com.weatheractivityplanner.weather_activity_planner_api.service.forecast;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.weatheractivityplanner.weather_activity_planner_api.client.OpenMeteoForecastClient;
import com.weatheractivityplanner.weather_activity_planner_api.client.OpenMeteoGeoCodingClient;
import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding.GeocodingResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding.GeocodingResultDTO;
import com.weatheractivityplanner.weather_activity_planner_api.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherForecastServiceImpl implements WeatherForecastService {

    private final OpenMeteoForecastClient openMeteoForecastClient;

    private final OpenMeteoGeoCodingClient openMeteoGeoCodingClient;

    @Override
    public ForecastResponseDTO getForecast(String city, String country, String timezone) {

        GeocodingResponseDTO geocodingResponseDTO = openMeteoGeoCodingClient.searchCity(city);

        if (geocodingResponseDTO == null
                || geocodingResponseDTO.getResults() == null
                || geocodingResponseDTO.getResults().isEmpty()) {

            throw new ResourceNotFoundException(
                    "No results found for city: " + city);

        }

        List<GeocodingResultDTO> results = geocodingResponseDTO.getResults();

        Optional<GeocodingResultDTO> selectedCity = results.stream()
                .filter(result -> result.getCountry() != null
                        && result.getCountry().equalsIgnoreCase(country))
                .findFirst();

        if (selectedCity.isEmpty()) {
            throw new ResourceNotFoundException(
                    "City " + city + " not found in country " + country);
        }

        GeocodingResultDTO cityResult = selectedCity.get();

        ForecastResponseDTO forecastResponseDTO = openMeteoForecastClient.getForecast(cityResult.getLatitude(),
                cityResult.getLongitude(), timezone);

        if (forecastResponseDTO == null) {
                throw new IllegalStateException(
                        "Weather forecast provider returned no data");
        }        

        return forecastResponseDTO;

    }

}
