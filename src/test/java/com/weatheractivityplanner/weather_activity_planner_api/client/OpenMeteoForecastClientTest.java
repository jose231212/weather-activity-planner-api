package com.weatheractivityplanner.weather_activity_planner_api.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import com.weatheractivityplanner.weather_activity_planner_api.dto.forecast.ForecastResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.exception.WeatherProviderException;

public class OpenMeteoForecastClientTest {

    private final RestClient.Builder builder = RestClient.builder()
            .baseUrl("https://api.open-meteo.com");

    private final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();

    private final OpenMeteoForecastClient client = new OpenMeteoForecastClient(builder.build());

    @Test
    void shouldReturnForecastWhenRequestIsSuccessful() {

        server.expect(requestTo(
                "https://api.open-meteo.com/v1/forecast?latitude=18.4861&longitude=-69.9312&timezone=America/Santo_Domingo&hourly=temperature_2m,precipitation_probability,wind_speed_10m,weather_code&format=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        """
                                {
                                    "latitude": 18.4861,
                                    "longitude": -69.9312,
                                    "timezone": "America/Santo_Domingo",
                                    "hourly": {
                                        "time": ["2026-11-15T14:00"],
                                        "temperature_2m": [29.4],
                                        "precipitation_probability": [20],
                                        "wind_speed_10m": [14.2],
                                        "weather_code": [2]
                                    }
                                }
                                """, MediaType.APPLICATION_JSON));

        ForecastResponseDTO response = client.getForecast(18.4861, -69.9312, "America/Santo_Domingo");

        assertNotNull(response);
        assertNotNull(response.getHourly());
        assertEquals(29.4, response.getHourly().getTemperature_2m().get(0));
        assertEquals(20, response.getHourly().getPrecipitation_probability().get(0));
        assertEquals(14.2, response.getHourly().getWind_speed_10m().get(0));
        assertEquals(2, response.getHourly().getWeather_code().get(0));
        assertEquals(LocalDateTime.of(2026, 11, 15, 14, 0),
                response.getHourly().getTime().get(0));

        server.verify();

    }

    @Test
    void shouldThrowExceptionWhenProviderReturnsServerError() {

            server.expect(requestTo(
                            "https://api.open-meteo.com/v1/forecast?latitude=18.4861&longitude=-69.9312&timezone=America/Santo_Domingo&hourly=temperature_2m,precipitation_probability,wind_speed_10m,weather_code&format=json"))
                            .andExpect(method(HttpMethod.GET))
                            .andRespond(withServerError());

            WeatherProviderException exception = assertThrows(
                            WeatherProviderException.class,
                            () -> client.getForecast(
                                            18.4861,
                                            -69.9312,
                                            "America/Santo_Domingo"));
            
            assertEquals(
                            "Weather provider returned an HTTP error",
                            exception.getMessage());                               
    }

    @Test
    void shouldThrowExceptionWhenConnectionFails() {

        server.expect(requestTo(
                "https://api.open-meteo.com/v1/forecast?latitude=18.4861&longitude=-69.9312&timezone=America/Santo_Domingo&hourly=temperature_2m,precipitation_probability,wind_speed_10m,weather_code&format=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request -> {
                    throw new java.io.IOException("Connection failed");
                });

    }

    @Test 
    void shouldThrowExceptionWhenProviderReturnsInvalidJson() {
            server.expect(requestTo(
                "https://api.open-meteo.com/v1/forecast?latitude=18.4861&longitude=-69.9312&timezone=America/Santo_Domingo&hourly=temperature_2m,precipitation_probability,wind_speed_10m,weather_code&format=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{ invalid json", MediaType.APPLICATION_JSON));

                assertThrows(WeatherProviderException.class,
                        () -> client.getForecast(
                                18.4861,
                                -69.9312,
                                "America/Santo_Domingo"));

                server.verify();
    }

    @Test
    void shouldReturnNullWhenProviderReturnsEmptyBody() {

            server.expect(requestTo(
            "https://api.open-meteo.com/v1/forecast?latitude=18.4861&longitude=-69.9312&timezone=America/Santo_Domingo&hourly=temperature_2m,precipitation_probability,wind_speed_10m,weather_code&format=json"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess());

            ForecastResponseDTO response = client.getForecast(
                    18.4861,
                    -69.9312,
                    "America/Santo_Domingo");

            assertNull(response);

            server.verify();

    }
}
