package com.weatheractivityplanner.weather_activity_planner_api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventRequestDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.entity.Event;
import com.weatheractivityplanner.weather_activity_planner_api.entity.enums.ActivityType;
import com.weatheractivityplanner.weather_activity_planner_api.exception.ResourceNotFoundException;
import com.weatheractivityplanner.weather_activity_planner_api.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

        @Mock
        private EventRepository eventRepository;

        @InjectMocks
        private EventServiceImpl eventServiceImpl;

        @Test
        void shouldCreateEventSuccessfully() {

                EventRequestDTO request = new EventRequestDTO();
                request.setName("Weekend Football Match");
                request.setActivityType(ActivityType.SPORTS);
                request.setCity("Punta Cana");
                request.setCountry("Dominican Republic");
                request.setLatitude(18.5601);
                request.setLongitude(-68.3725);
                request.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                request.setMinTemperature(20.0);
                request.setMaxTemperature(32.0);
                request.setMaxPrecipitationProbability(30);
                request.setMaxWindSpeed(25.0);
                request.setReschedulable(true);

                when(eventRepository.save(any(Event.class)))
                                .thenAnswer(invocation -> {
                                        Event event = invocation.getArgument(0);
                                        event.setId(1L);
                                        return event;
                                });

                EventResponseDTO responseDTO = eventServiceImpl.createEvent(request);

                assertNotNull(responseDTO);
                assertEquals(1L, responseDTO.getId());
                assertEquals(request.getName(), responseDTO.getName());
                assertEquals(request.getActivityType(), responseDTO.getActivityType());
                assertEquals(request.getCity(), responseDTO.getCity());
                assertEquals(request.getCountry(), responseDTO.getCountry());
                assertEquals(request.getLatitude(), responseDTO.getLatitude());
                assertEquals(request.getLongitude(), responseDTO.getLongitude());
                assertEquals(request.getScheduledAt(), responseDTO.getScheduledAt());
                assertEquals(request.getMinTemperature(), responseDTO.getMinTemperature());
                assertEquals(request.getMaxTemperature(), responseDTO.getMaxTemperature());
                assertEquals(request.getMaxPrecipitationProbability(), responseDTO.getMaxPrecipitationProbability());
                assertEquals(request.getMaxWindSpeed(), responseDTO.getMaxWindSpeed());
                assertEquals(request.getScheduledAt(), responseDTO.getScheduledAt());

                verify(eventRepository, times(1)).save(any(Event.class));

        }

        @Test
        void shouldReturnAllEventsSuccessfully() {

                Event event1 = new Event();
                event1.setId(1L);
                event1.setName("Weekend Football Match");
                event1.setActivityType(ActivityType.SPORTS);
                event1.setCity("Punta Cana");
                event1.setCountry("Dominican Republic");
                event1.setLatitude(18.5601);
                event1.setLongitude(-68.3725);
                event1.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                event1.setMinTemperature(20.0);
                event1.setMaxTemperature(32.0);
                event1.setMaxPrecipitationProbability(30);
                event1.setMaxWindSpeed(25.0);
                event1.setReschedulable(true);

                Event event2 = new Event();
                event2.setId(2L);
                event2.setName("Outdoor Music Festival");
                event2.setActivityType(ActivityType.OUTDOOR_EVENT);
                event2.setCity("Santo Domingo");
                event2.setCountry("Dominican Republic");
                event2.setLatitude(18.4861);
                event2.setLongitude(-69.9312);
                event2.setScheduledAt(LocalDateTime.of(2026, 11, 20, 18, 30));
                event2.setMinTemperature(18.0);
                event2.setMaxTemperature(30.0);
                event2.setMaxPrecipitationProbability(20);
                event2.setMaxWindSpeed(15.0);
                event2.setReschedulable(true);

                Pageable pageable = PageRequest.of(0, 2);

                Page<Event> eventPage = new PageImpl<>(
                                List.of(event1, event2),
                                pageable,
                                2);

                when(eventRepository.findAll(pageable))
                                .thenReturn(eventPage);

                Page<EventResponseDTO> pageResult = eventServiceImpl.getAllEvents(pageable);

                assertEquals(2, pageResult.getContent().size());
                assertEquals(2, pageResult.getTotalElements());
                assertEquals(1, pageResult.getTotalPages());
                assertEquals(2, pageResult.getSize());

                assertEquals(event1.getName(), pageResult.getContent().get(0).getName());
                assertEquals(event1.getActivityType(), pageResult.getContent().get(0).getActivityType());
                assertEquals(event1.getCity(), pageResult.getContent().get(0).getCity());
                assertEquals(event1.getCountry(), pageResult.getContent().get(0).getCountry());
                assertEquals(event1.getLatitude(), pageResult.getContent().get(0).getLatitude());
                assertEquals(event1.getLongitude(), pageResult.getContent().get(0).getLongitude());
                assertEquals(event1.getScheduledAt(), pageResult.getContent().get(0).getScheduledAt());
                assertEquals(event1.getMinTemperature(), pageResult.getContent().get(0).getMinTemperature());
                assertEquals(event1.getMaxTemperature(), pageResult.getContent().get(0).getMaxTemperature());
                assertEquals(event1.getMaxPrecipitationProbability(),
                                pageResult.getContent().get(0).getMaxPrecipitationProbability());
                assertEquals(event1.getMaxWindSpeed(), pageResult.getContent().get(0).getMaxWindSpeed());
                assertEquals(event1.isReschedulable(), pageResult.getContent().get(0).isReschedulable());

                assertEquals(event2.getName(), pageResult.getContent().get(1).getName());
                assertEquals(event2.getActivityType(), pageResult.getContent().get(1).getActivityType());
                assertEquals(event2.getCity(), pageResult.getContent().get(1).getCity());
                assertEquals(event2.getCountry(), pageResult.getContent().get(1).getCountry());
                assertEquals(event2.getLatitude(), pageResult.getContent().get(1).getLatitude());
                assertEquals(event2.getLongitude(), pageResult.getContent().get(1).getLongitude());
                assertEquals(event2.getScheduledAt(), pageResult.getContent().get(1).getScheduledAt());
                assertEquals(event2.getMinTemperature(), pageResult.getContent().get(1).getMinTemperature());
                assertEquals(event2.getMaxTemperature(), pageResult.getContent().get(1).getMaxTemperature());
                assertEquals(event2.getMaxPrecipitationProbability(),
                                pageResult.getContent().get(1).getMaxPrecipitationProbability());
                assertEquals(event2.getMaxWindSpeed(), pageResult.getContent().get(1).getMaxWindSpeed());
                assertEquals(event2.isReschedulable(), pageResult.getContent().get(1).isReschedulable());

                verify(eventRepository, times(1)).findAll(pageable);

        }

        @Test
        void shouldReturnEmptyPageWhenNoEventExists() {

                Pageable pageable = PageRequest.of(0, 2);

                Page<Event> eventPage = new PageImpl<>(
                                List.of(),
                                pageable,
                                0);

                when(eventRepository.findAll(pageable))
                                .thenReturn(eventPage);

                Page<EventResponseDTO> result = eventServiceImpl.getAllEvents(pageable);

                assertTrue(result.isEmpty());

                assertEquals(0, result.getTotalElements());
                assertEquals(0, result.getContent().size());

                verify(eventRepository, times(1)).findAll(pageable);

        }

        @Test
        void shouldReturnEventByIdSuccessfully() {

                Event event1 = new Event();
                event1.setId(1L);
                event1.setName("Weekend Football Match");
                event1.setActivityType(ActivityType.SPORTS);
                event1.setCity("Punta Cana");
                event1.setCountry("Dominican Republic");
                event1.setLatitude(18.5601);
                event1.setLongitude(-68.3725);
                event1.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                event1.setMinTemperature(20.0);
                event1.setMaxTemperature(32.0);
                event1.setMaxPrecipitationProbability(30);
                event1.setMaxWindSpeed(25.0);
                event1.setReschedulable(true);

                when(eventRepository.findById(1L))
                                .thenReturn(Optional.of(event1));

                EventResponseDTO result = eventServiceImpl.getEventById(1L);

                assertEquals(event1.getId(), result.getId());
                assertEquals(event1.getName(), result.getName());
                assertEquals(event1.getActivityType(), result.getActivityType());
                assertEquals(event1.getCity(), result.getCity());
                assertEquals(event1.getCountry(), result.getCountry());
                assertEquals(event1.getLatitude(), result.getLatitude());
                assertEquals(event1.getLongitude(), result.getLongitude());
                assertEquals(event1.getScheduledAt(), result.getScheduledAt());
                assertEquals(event1.getMinTemperature(), result.getMinTemperature());
                assertEquals(event1.getMaxTemperature(), result.getMaxTemperature());
                assertEquals(event1.getMaxPrecipitationProbability(), result.getMaxPrecipitationProbability());
                assertEquals(event1.getMaxWindSpeed(), result.getMaxWindSpeed());
                assertEquals(event1.isReschedulable(), result.isReschedulable());

                verify(eventRepository, times(1)).findById(1L);

        }

        @Test
        void shouldThrowExceptionWhenEventDoesNotExist() {

                when(eventRepository.findById(1L))
                                .thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> eventServiceImpl.getEventById(1L));

                assertEquals("Event not found with id: 1", exception.getMessage());

                verify(eventRepository, times(1)).findById(1L);

        }

        @Test
        void shouldUpdateEventSuccessfully() {

                Event event1 = new Event();
                event1.setId(1L);
                event1.setName("Weekend Football Match");
                event1.setActivityType(ActivityType.SPORTS);
                event1.setCity("Punta Cana");
                event1.setCountry("Dominican Republic");
                event1.setLatitude(18.5601);
                event1.setLongitude(-68.3725);
                event1.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                event1.setMinTemperature(20.0);
                event1.setMaxTemperature(32.0);
                event1.setMaxPrecipitationProbability(30);
                event1.setMaxWindSpeed(25.0);
                event1.setReschedulable(true);

                when(eventRepository.findById(1L))
                                .thenReturn(Optional.of(event1));

                EventRequestDTO eventRequestDTO = new EventRequestDTO();

                eventRequestDTO.setName("Weekend Baseball Match");
                eventRequestDTO.setActivityType(ActivityType.SPORTS);
                eventRequestDTO.setCity("Punta Cana");
                eventRequestDTO.setCountry("Dominican Republic");
                eventRequestDTO.setLatitude(18.5601);
                eventRequestDTO.setLongitude(-68.3725);
                eventRequestDTO.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                eventRequestDTO.setMinTemperature(20.0);
                eventRequestDTO.setMaxTemperature(32.0);
                eventRequestDTO.setMaxPrecipitationProbability(30);
                eventRequestDTO.setMaxWindSpeed(25.0);
                eventRequestDTO.setReschedulable(true);

                when(eventRepository.save(any(Event.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                EventResponseDTO result = eventServiceImpl.updateEvent(event1.getId(), eventRequestDTO);

                assertEquals(event1.getId(), result.getId());
                assertEquals(eventRequestDTO.getName(), result.getName());
                assertEquals(eventRequestDTO.getActivityType(), result.getActivityType());
                assertEquals(eventRequestDTO.getCity(), result.getCity());
                assertEquals(eventRequestDTO.getCountry(), result.getCountry());
                assertEquals(eventRequestDTO.getLatitude(), result.getLatitude());
                assertEquals(eventRequestDTO.getLongitude(), result.getLongitude());
                assertEquals(eventRequestDTO.getScheduledAt(), result.getScheduledAt());
                assertEquals(eventRequestDTO.getMinTemperature(), result.getMinTemperature());
                assertEquals(eventRequestDTO.getMaxTemperature(), result.getMaxTemperature());
                assertEquals(eventRequestDTO.getMaxPrecipitationProbability(), result.getMaxPrecipitationProbability());
                assertEquals(eventRequestDTO.getMaxWindSpeed(), result.getMaxWindSpeed());
                assertEquals(eventRequestDTO.isReschedulable(), result.isReschedulable());

                verify(eventRepository, times(1)).findById(1L);
                verify(eventRepository, times(1)).save(any(Event.class));

        }

        @Test
        void shouldThrowExceptionWhenUpdatingEventDoesNotExist() {
                when(eventRepository.findById(1L))
                                .thenReturn(Optional.empty());

                EventRequestDTO eventRequestDTO = new EventRequestDTO();
                eventRequestDTO.setMinTemperature(20.0);
                eventRequestDTO.setMaxTemperature(30.0);

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> eventServiceImpl.updateEvent(1L, eventRequestDTO));

                assertEquals("Event not found with id: 1", exception.getMessage());

                verify(eventRepository, times(1)).findById(1L);
                verify(eventRepository, never()).save(any(Event.class));
        }

        @Test
        void shouldDeleteEventSuccessfully() {

                Event event1 = new Event();
                event1.setId(1L);
                event1.setName("Weekend Football Match");
                event1.setActivityType(ActivityType.SPORTS);
                event1.setCity("Punta Cana");
                event1.setCountry("Dominican Republic");
                event1.setLatitude(18.5601);
                event1.setLongitude(-68.3725);
                event1.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                event1.setMinTemperature(20.0);
                event1.setMaxTemperature(32.0);
                event1.setMaxPrecipitationProbability(30);
                event1.setMaxWindSpeed(25.0);
                event1.setReschedulable(true);

                when(eventRepository.findById(1L))
                                .thenReturn(Optional.of(event1));

                eventServiceImpl.deleteEvent(1L);

                verify(eventRepository, times(1)).findById(1L);
                verify(eventRepository).delete(event1);

        }

        @Test
        void shouldThrowExceptionWhenDeletingEventDoesNotExist() {

                when(eventRepository.findById(1L))
                                .thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                () -> eventServiceImpl.deleteEvent(1L));

                assertEquals("Event not found with id: 1", exception.getMessage());

                verify(eventRepository, times(1)).findById(1L);
                verify(eventRepository, never()).delete(any(Event.class));
        }

        @Test
        void shouldThrowExceptionWhenCreatingEventWithInvalidTemperatureRange() {

                EventRequestDTO request = new EventRequestDTO();
                request.setName("Weekend Football Match");
                request.setActivityType(ActivityType.SPORTS);
                request.setCity("Punta Cana");
                request.setCountry("Dominican Republic");
                request.setLatitude(18.5601);
                request.setLongitude(-68.3725);
                request.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                request.setMinTemperature(30.0);
                request.setMaxTemperature(20.0);
                request.setMaxPrecipitationProbability(30);
                request.setMaxWindSpeed(25.0);
                request.setReschedulable(true);

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                () -> eventServiceImpl.createEvent(request));

                assertEquals("Minimum temperature cannot be greater than maximum temperature", exception.getMessage());

                verify(eventRepository, never()).save(any(Event.class));
        }

        @Test
        void shouldThrowExceptionWhenUpdatingEventWithInvalidTemperatureRange() {

                EventRequestDTO request = new EventRequestDTO();
                request.setName("Weekend Football Match");
                request.setActivityType(ActivityType.SPORTS);
                request.setCity("Punta Cana");
                request.setCountry("Dominican Republic");
                request.setLatitude(18.5601);
                request.setLongitude(-68.3725);
                request.setScheduledAt(LocalDateTime.of(2026, 11, 15, 16, 0));
                request.setMinTemperature(30.0);
                request.setMaxTemperature(20.0);
                request.setMaxPrecipitationProbability(30);
                request.setMaxWindSpeed(25.0);
                request.setReschedulable(true);

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                () -> eventServiceImpl.updateEvent(1L, request));

                assertEquals("Minimum temperature cannot be greater than maximum temperature", exception.getMessage());

                verify(eventRepository, never()).save(any(Event.class));
        }

}
