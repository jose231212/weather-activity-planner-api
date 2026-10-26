package com.weatheractivityplanner.weather_activity_planner_api.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenMeteoForecastClient {

    @Qualifier("restClientForecast")
    private final RestClient restClientForecast;

    public ForecastResponseDTO getForecast(double latitude, double longitude, String timezone) {

        return restClientForecast.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/v1/forecast")
                    .queryParam("latitude", latitude)
                    .queryParam("longitude", longitude)
                    .queryParam("timezone", timezone)
                    .queryParam("hourly", "temperature_2m,precipitation_probability,wind_speed_10m,weather_code")
                    .queryParam("format", "json")
                    .build())
                    .retrieve()
                    .body(ForecastResponseDTO.class);

    }

}
