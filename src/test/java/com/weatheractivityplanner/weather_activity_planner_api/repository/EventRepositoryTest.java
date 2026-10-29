package com.weatheractivityplanner.weather_activity_planner_api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;

import com.weatheractivityplanner.weather_activity_planner_api.entity.Event;
import com.weatheractivityplanner.weather_activity_planner_api.entity.enums.ActivityType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldSaveAndRetrieveEvent() {

        Event event = new Event();
        event.setName("Football match");
        event.setActivityType(ActivityType.SPORTS);
        event.setCity("Higuey");
        event.setCountry("Dominican Republic");
        event.setLatitude(18.615);
        event.setLongitude(-68.707);
        event.setScheduledAt(LocalDateTime.now().plusDays(7));
        event.setMinTemperature(18.0);
        event.setMaxTemperature(32.0);
        event.setMaxPrecipitationProbability(30);
        event.setMaxWindSpeed(25.0);
        event.setReschedulable(true);

        Event savedEvent = eventRepository.save(event);

        Optional<Event> result = eventRepository.findById(savedEvent.getId());

        assertNotNull(savedEvent.getId());
        assertTrue(result.isPresent());

        Event retrievedEvent = result.get();

        assertEquals(savedEvent.getId(), result.get().getId());
        assertEquals("Football match", retrievedEvent.getName());
        assertEquals(ActivityType.SPORTS, retrievedEvent.getActivityType());
        assertEquals(savedEvent.getCity(), retrievedEvent.getCity());
        assertEquals(savedEvent.getCountry(), retrievedEvent.getCountry());
        assertEquals(savedEvent.getLatitude(), retrievedEvent.getLatitude(), 0.001);
        assertEquals(savedEvent.getLongitude(), retrievedEvent.getLongitude(), 0.001);
        assertEquals(savedEvent.getScheduledAt(), retrievedEvent.getScheduledAt());
        assertEquals(18.0, retrievedEvent.getMinTemperature(), 0.001);
        assertEquals(32.0, retrievedEvent.getMaxTemperature(), 0.001);
        assertEquals(savedEvent.getMaxPrecipitationProbability(),
                retrievedEvent.getMaxPrecipitationProbability());
        assertEquals(25.0, retrievedEvent.getMaxWindSpeed(), 0.001);
        assertEquals(savedEvent.isReschedulable(), retrievedEvent.isReschedulable());
    }

}
