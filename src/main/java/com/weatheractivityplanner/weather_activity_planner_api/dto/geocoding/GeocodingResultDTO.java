package com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class GeocodingResultDTO {

    private String name;

    private double latitude;

    private double longitude;

    private String country;

    private String admin1;

}
