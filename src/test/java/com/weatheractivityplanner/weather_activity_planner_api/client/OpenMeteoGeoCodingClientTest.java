package com.weatheractivityplanner.weather_activity_planner_api.client;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.weatheractivityplanner.weather_activity_planner_api.dto.geocoding.GeocodingResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.exception.WeatherProviderException;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

public class OpenMeteoGeoCodingClientTest {

    private final RestClient.Builder builder = RestClient.builder()
            .baseUrl("https://geocoding-api.open-meteo.com");

    private final MockRestServiceServer server = 
            MockRestServiceServer.bindTo(builder).build();

    private final OpenMeteoGeoCodingClient client =
            new OpenMeteoGeoCodingClient(builder.build());

    @Test 
    void shouldReturnGeoCodingResultsWhenCityExists() {

        server.expect(requestTo("https://geocoding-api.open-meteo.com/v1/search?name=Santo%20Domingo&count=5&language=es&format=json"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("""
                {
                    "results": [
                        {
                            "name": "Santo Domingo",
                            "latitude": 18.4861,
                            "longitude": -69.9312,
                            "country": "Dominican Republic",
                            "admin1": "Distrito Nacional"
                        }
                    ]
                }
                """, MediaType.APPLICATION_JSON));

        GeocodingResponseDTO response = client.searchCity("Santo Domingo");

        assertNotNull(response.getResults());
        assertEquals(1, response.getResults().size());
        assertEquals("Santo Domingo", response.getResults().get(0).getName());
        server.verify();

    }

    @Test 
    void shouldReturnNullResultsWhenCityDoesNotExist() {

        server.expect(requestTo("https://geocoding-api.open-meteo.com/v1/search?name=CiudadInexistente&count=5&language=es&format=json"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("""
                {
                    "results": null
                }
                """, MediaType.APPLICATION_JSON));

        GeocodingResponseDTO response = client.searchCity("CiudadInexistente");

        assertNotNull(response);
        assertNull(response.getResults());
        server.verify();

    }

    @Test
    void shouldThrowExceptionWhenGeocodingApiRetursServerError() {

        server.expect(requestTo("https://geocoding-api.open-meteo.com/v1/search?name=Santo%20Domingo&count=5&language=es&format=json"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withServerError());

        assertThrows(WeatherProviderException.class, () -> {
            client.searchCity("Santo Domingo");
        });
        server.verify();

    }

    @Test
    void shouldThrowExceptionWhenGeocodingConnectionFails() {

        server.expect(requestTo(
                "https://geocoding-api.open-meteo.com/v1/search?name=Santo%20Domingo&count=5&language=es&format=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request -> {
                    throw new IOException("Connection failed");
                });

        WeatherProviderException exception = assertThrows(
                WeatherProviderException.class,
                () -> client.searchCity("Santo Domingo"));

        assertEquals(
                "Could not connect to geocoding provider",
                exception.getMessage());

        server.verify();
    }

    @Test
    void shouldThrowExceptionWhenGeocodingReturnsInvalidJson() {

        server.expect(requestTo(
                "https://geocoding-api.open-meteo.com/v1/search?name=Santo%20Domingo&count=5&language=es&format=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{ invalid json",
                        MediaType.APPLICATION_JSON));

        WeatherProviderException exception = assertThrows(
                WeatherProviderException.class,
                () -> client.searchCity("Santo Domingo"));

        assertEquals(
                "Geocoding provider returned an invalid response",
                exception.getMessage());

        server.verify();
    }

    @Test
    void shouldReturnNullWhenGeocodingProviderReturnsEmptyBody() {

        server.expect(requestTo(
                "https://geocoding-api.open-meteo.com/v1/search?name=Santo%20Domingo&count=5&language=es&format=json"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        GeocodingResponseDTO response = client.searchCity("Santo Domingo");

        assertNull(response);

        server.verify();
    }

}
