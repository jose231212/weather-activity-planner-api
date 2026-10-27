package com.weatheractivityplanner.weather_activity_planner_api.service.forecast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weatheractivityplanner.weather_activity_planner_api.client.OpenMeteoForecastClient;
import com.weatheractivityplanner.weather_activity_planner_api.client.OpenMeteoGeoCodingClient;
import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.HourlyForecastDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding.GeocodingResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding.GeocodingResultDTO;
import com.weatheractivityplanner.weather_activity_planner_api.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class WeatherForeCastServiceImplTest {

    @Mock
    private OpenMeteoGeoCodingClient openMeteoGeoCodingClient;

    @Mock
    private OpenMeteoForecastClient openMeteoForecastClient;

    @InjectMocks
    private WeatherForecastServiceImpl weatherForecastServiceImpl;

    @Test
    void shouldReturnForecastWhenCityAndCountryExist() {

        //prepare the geocoding response
        GeocodingResultDTO cityResult = new GeocodingResultDTO();
        cityResult.setName("Santo Domingo");
        cityResult.setLatitude(18.4861);
        cityResult.setLongitude(-69.9312);
        cityResult.setCountry("Dominican Republic");

        GeocodingResponseDTO geocodingResponse = new GeocodingResponseDTO();
        geocodingResponse.setResults(List.of(cityResult));

        when(openMeteoGeoCodingClient.searchCity("Santo Domingo"))
                .thenReturn(geocodingResponse);

        //prepare the forecast response
        HourlyForecastDTO hourlyForecast = new HourlyForecastDTO();
        hourlyForecast.setTemperature_2m(List.of(29.4));

        ForecastResponseDTO expectedForecast = new ForecastResponseDTO();
        expectedForecast.setLatitude(18.4861);
        expectedForecast.setLongitude(-69.9312);
        expectedForecast.setTimezone("America/Santo_Domingo");
        expectedForecast.setHourly(hourlyForecast);

        when(openMeteoForecastClient.getForecast(
                18.4861,
                -69.9312,
                "America/Santo_Domingo"))
                .thenReturn(expectedForecast);

        //execute the service
        ForecastResponseDTO actualForecast = weatherForecastServiceImpl.getForecast(
                "Santo Domingo",
                "Dominican Republic",
                "America/Santo_Domingo");

        //verify the result
        assertNotNull(actualForecast);
        assertEquals(18.4861, actualForecast.getLatitude());
        assertEquals(-69.9312, actualForecast.getLongitude());
        assertEquals("America/Santo_Domingo", actualForecast.getTimezone());
        assertNotNull(actualForecast.getHourly());
        assertEquals(29.4,
                actualForecast.getHourly().getTemperature_2m().get(0));

        verify(openMeteoGeoCodingClient).searchCity("Santo Domingo");
        verify(openMeteoForecastClient).getForecast(
                18.4861,
                -69.9312,
                "America/Santo_Domingo");


    }

    @Test 
    void shouldThrowExceptionWhenCityHasNoResults() {

        GeocodingResponseDTO geocodingResponse = new GeocodingResponseDTO();
        geocodingResponse.setResults(List.of());

        when(openMeteoGeoCodingClient.searchCity("CiudadInexistente"))
                .thenReturn(geocodingResponse);

        assertThrows(ResourceNotFoundException.class, 
                    () -> weatherForecastServiceImpl.getForecast(
                        "CiudadInexistente", 
                        "Dominican Republic", 
                    "America/Santo_Domingo")
        );

        verify(openMeteoForecastClient, never())
                .getForecast(
                    18.4861, 
                    -69.9312, 
                "America/Santo_Domingo");

    }

    @Test
    void shouldThrowExceptionWhenCityDoesNotBelongToCountry() {

            GeocodingResultDTO cityResult = new GeocodingResultDTO();
            cityResult.setName("Santiago");
            cityResult.setLatitude(-33.4489);
            cityResult.setLongitude(-70.6693);
            cityResult.setCountry("Chile");

            GeocodingResponseDTO geocodingResponseDTO = new GeocodingResponseDTO();
            geocodingResponseDTO.setResults(List.of(cityResult));

            when(openMeteoGeoCodingClient.searchCity("Santiago"))
                            .thenReturn(geocodingResponseDTO);

            assertThrows(ResourceNotFoundException.class,
                            () -> weatherForecastServiceImpl.getForecast(
                                            "Santiago",
                                            "Dominican Republic",
                                            "America/Santo_Domingo"));

            verify(openMeteoForecastClient, never())
                            .getForecast(
                                            -33.4489,
                                            -70.6693,
                                            "America/Santo_Domingo");

    }

    @Test
    void shouldThrowExceptionWhenForecastResponseIsNull() {

            GeocodingResultDTO cityResult = new GeocodingResultDTO();
            cityResult.setName("Santo Domingo");
            cityResult.setLatitude(18.4861);
            cityResult.setLongitude(-69.9312);
            cityResult.setCountry("Dominican Republic");

            GeocodingResponseDTO geocodingResponseDTO = new GeocodingResponseDTO();
            geocodingResponseDTO.setResults(List.of(cityResult));

            when(openMeteoGeoCodingClient.searchCity("Santo Domingo"))
                            .thenReturn(geocodingResponseDTO);

            when(openMeteoForecastClient.getForecast(
                            18.4861,
                            -69.9312,
                            "America/Santo_Domingo"))
                            .thenReturn(null);

            assertThrows(
                            IllegalStateException.class,
                            () -> weatherForecastServiceImpl.getForecast(
                                            "Santo Domingo",
                                            "Dominican Republic",
                                            "America/Santo_Domingo"));

            verify(openMeteoForecastClient).getForecast(
                            18.4861,
                            -69.9312,
                            "America/Santo_Domingo");

    }


}
