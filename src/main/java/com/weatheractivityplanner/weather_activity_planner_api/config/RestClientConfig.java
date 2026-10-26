package com.weatheractivityplanner.weather_activity_planner_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration 
public class RestClientConfig {

    @Value("${open-meteo.geocoding.base-url}")
    private String geoCoding;

    @Value("${open-meteo.forecast.base-url}")
    private String forecast;

    @Bean 
    public RestClient restClientGeo() {

        return RestClient.builder()
                .baseUrl(geoCoding)
                .build();

    }

    @Bean 
    public RestClient restClientForecast() {

        return RestClient.builder()
                .baseUrl(forecast)
                .build();

    }
 
}
