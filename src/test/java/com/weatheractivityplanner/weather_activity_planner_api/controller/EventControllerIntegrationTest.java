package com.weatheractivityplanner.weather_activity_planner_api.controller;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;
import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventRequestDTO;
import com.weatheractivityplanner.weather_activity_planner_api.entity.Event;
import com.weatheractivityplanner.weather_activity_planner_api.entity.enums.ActivityType;
import com.weatheractivityplanner.weather_activity_planner_api.repository.EventRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach 
    void setUp() {
        eventRepository.deleteAll();
    }

    @Test
    void shouldCreateEventSuccessfully() throws Exception {

        EventRequestDTO request = new EventRequestDTO();

        request.setName("Weekend Football Match");
        request.setActivityType(ActivityType.SPORTS);
        request.setCity("Punta Cana");
        request.setCountry("Dominican Republic");
        request.setLatitude(18.5601);
        request.setLongitude(-68.3725);
        request.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
        request.setMinTemperature(20.0);
        request.setMaxTemperature(30.00);
        request.setMaxPrecipitationProbability(30);
        request.setMaxWindSpeed(25.0);
        request.setReschedulable(true);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Weekend Football Match"))
                .andExpect(jsonPath("$.activityType").value("SPORTS"))
                .andExpect(jsonPath("$.city").value("Punta Cana"))
                .andExpect(jsonPath("$.country").value("Dominican Republic"))
                .andExpect(jsonPath("$.reschedulable").value(true));

    }

    @Test
    void shouldReturnEventByIdSuccessfully() throws Exception {

        Event event = new Event();

        event.setName("Weekend Football Match");
        event.setActivityType(ActivityType.SPORTS);
        event.setCity("Punta Cana");
        event.setCountry("Dominican Republic");
        event.setLatitude(18.5601);
        event.setLongitude(-68.3725);
        event.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
        event.setMinTemperature(20.0);
        event.setMaxTemperature(30.0);
        event.setMaxPrecipitationProbability(30);
        event.setMaxWindSpeed(25.0);
        event.setReschedulable(true);

        Event savedEvent = eventRepository.save(event);

        mockMvc.perform(get("/api/events/" + savedEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEvent.getId()))
                .andExpect(jsonPath("$.name").value("Weekend Football Match"))
                .andExpect(jsonPath("$.activityType").value("SPORTS"))
                .andExpect(jsonPath("$.city").value("Punta Cana"))
                .andExpect(jsonPath("$.country").value("Dominican Republic"))
                .andExpect(jsonPath("$.reschedulable").value(true));

    }

    @Test
    void shouldReturnAllEventsSuccessfully() throws Exception {

        Event event = new Event();

        event.setName("Weekend Football Match");
        event.setActivityType(ActivityType.SPORTS);
        event.setCity("Punta Cana");
        event.setCountry("Dominican Republic");
        event.setLatitude(18.5601);
        event.setLongitude(-68.3725);
        event.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
        event.setMinTemperature(20.0);
        event.setMaxTemperature(30.0);
        event.setMaxPrecipitationProbability(30);
        event.setMaxWindSpeed(25.0);
        event.setReschedulable(true);

        Event event2 = new Event();

        event2.setName("Family Picnic");
        event2.setActivityType(ActivityType.PICNIC);
        event2.setCity("La Romana");
        event2.setCountry("Dominican Republic");
        event2.setLatitude(18.4273);
        event2.setLongitude(-68.9728);
        event2.setScheduledAt(LocalDateTime.of(2026, 11, 20, 10, 0));
        event2.setMinTemperature(22.0);
        event2.setMaxTemperature(32.0);
        event2.setMaxPrecipitationProbability(20);
        event2.setMaxWindSpeed(15.0);
        event2.setReschedulable(false);

        eventRepository.save(event);
        eventRepository.save(event2);

        mockMvc.perform(get("/api/events")
                .param("page", "0")
                .param("size", "2")
                .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Weekend Football Match"))
                .andExpect(jsonPath("$.content[1].name").value("Family Picnic"))
                .andExpect(jsonPath("$.totalElements").value(2));

    }

    @Test
    void shouldReturnNotFoundWhenEventDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/events/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event not found with id: 99999"))
                .andExpect(jsonPath("$.errors").value(nullValue()));

    }

    @Test
    void shouldReturnBadRequestWhenCreatingEventWithInvalidData() throws Exception {

        EventRequestDTO request = new EventRequestDTO();

        request.setName("Weekend Football Match");
        request.setActivityType(ActivityType.SPORTS);
        request.setCity("Punta Cana");
        request.setCountry("Dominican Republic");
        request.setLatitude(18.5601);
        request.setLongitude(-68.3725);
        request.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
        request.setMinTemperature(20.0);
        request.setMaxTemperature(30.00);
        request.setMaxPrecipitationProbability(120);
        request.setMaxWindSpeed(25.0);
        request.setReschedulable(true);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.maxPrecipitationProbability")
                        .value("must be less than or equal to 100"));

    }

    @Test
    void shouldUpdateEventSuccessfully() throws Exception {

        Event event = new Event();

        event.setName("Weekend Football Match");
        event.setActivityType(ActivityType.SPORTS);
        event.setCity("Punta Cana");
        event.setCountry("Dominican Republic");
        event.setLatitude(18.5601);
        event.setLongitude(-68.3725);
        event.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
        event.setMinTemperature(20.0);
        event.setMaxTemperature(30.0);
        event.setMaxPrecipitationProbability(30);
        event.setMaxWindSpeed(25.0);
        event.setReschedulable(true);

        Event savedEvent = eventRepository.save(event);

        EventRequestDTO eventUpdated = new EventRequestDTO();
        eventUpdated.setName("Updated Football Match");
        eventUpdated.setCity("Santo Domingo");
        eventUpdated.setCountry("Dominican Republic");
        eventUpdated.setMinTemperature(18.0);
        eventUpdated.setMaxTemperature(28.0);
        eventUpdated.setMaxPrecipitationProbability(15);
        eventUpdated.setMaxWindSpeed(20.0);
        eventUpdated.setReschedulable(false);
        eventUpdated.setActivityType(ActivityType.SPORTS);
        eventUpdated.setLatitude(18.4861);
        eventUpdated.setLongitude(-69.9312);
        eventUpdated.setScheduledAt(LocalDateTime.of(2026, 11, 16, 17, 0));

        mockMvc.perform(put("/api/events/" + savedEvent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEvent.getId()))
                .andExpect(jsonPath("$.name").value("Updated Football Match"))
                .andExpect(jsonPath("$.city").value("Santo Domingo"))
                .andExpect(jsonPath("$.minTemperature").value(18.0))
                .andExpect(jsonPath("$.maxTemperature").value(28.0))
                .andExpect(jsonPath("$.maxPrecipitationProbability").value(15))
                .andExpect(jsonPath("$.maxWindSpeed").value(20.0))
                .andExpect(jsonPath("$.reschedulable").value(false));

    }

    @Test
    void shouldDeleteEventSuccessfully() throws Exception {

        Event event = new Event();

        event.setName("Weekend Football Match");
        event.setActivityType(ActivityType.SPORTS);
        event.setCity("Punta Cana");
        event.setCountry("Dominican Republic");
        event.setLatitude(18.5601);
        event.setLongitude(-68.3725);
        event.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
        event.setMinTemperature(20.0);
        event.setMaxTemperature(30.0);
        event.setMaxPrecipitationProbability(30);
        event.setMaxWindSpeed(25.0);
        event.setReschedulable(true);

        Event savedEvent = eventRepository.save(event);

        mockMvc.perform(delete("/api/events/" + savedEvent.getId()))
                .andExpect(status().isNoContent());

        assertFalse(eventRepository.existsById(savedEvent.getId()));

    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingEvent() throws Exception {

        EventRequestDTO request = new EventRequestDTO();

        request.setName("Weekend Football Match");
        request.setActivityType(ActivityType.SPORTS);
        request.setCity("Punta Cana");
        request.setCountry("Dominican Republic");
        request.setLatitude(18.5601);
        request.setLongitude(-68.3725);
        request.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
        request.setMinTemperature(20.0);
        request.setMaxTemperature(30.00);
        request.setMaxPrecipitationProbability(30);
        request.setMaxWindSpeed(25.0);
        request.setReschedulable(true);

        mockMvc.perform(put("/api/events/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event not found with id: 99999"))
                .andExpect(jsonPath("$.errors").value(nullValue()));

    }

}
