package com.weatheractivityplanner.weather_activity_planner_api.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding.GeocodingResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class OpenMeteoGeoCodingClient {

    @Qualifier("restClientGeo")
    private final RestClient restClientGeo;

    public GeocodingResponseDTO searchCity(String cityName) {

        return restClientGeo.get()
                    .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("name", cityName)
                        .queryParam("count", 5)
                        .queryParam("language", "es")
                        .queryParam("format", "json")
                        .build())
                        .retrieve()
                        .body(GeocodingResponseDTO.class);
                    
    }

}
