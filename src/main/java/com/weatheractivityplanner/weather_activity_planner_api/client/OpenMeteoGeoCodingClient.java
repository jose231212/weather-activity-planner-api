package com.weatheractivityplanner.weather_activity_planner_api.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding.GeocodingResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.exception.WeatherProviderException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class OpenMeteoGeoCodingClient {

    @Qualifier("restClientGeo")
    private final RestClient restClientGeo;

    public GeocodingResponseDTO searchCity(String cityName) {

        try {
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
        } catch (RestClientResponseException exception) {
            throw new WeatherProviderException(
                "Geocoding Provider returned an HTTP error"
            );
        } catch (ResourceAccessException exception) {
            throw new WeatherProviderException(
                "Could not connect to geocoding provider"
            );
        } catch (RestClientException exception) {
            throw new WeatherProviderException(
                "Geocoding provider returned an invalid response"
            );
        }
                    
    }

}
