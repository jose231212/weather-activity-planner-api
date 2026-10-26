package com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class GeocodingResponseDTO {

    private List<GeocodingResultDTO> results;

}
