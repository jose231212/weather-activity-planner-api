package com.weatheractivityplanner.weather_activity_planner_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventRequestDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventResponseDTO;

public interface EventService {

    EventResponseDTO createEvent(EventRequestDTO request);

    Page<EventResponseDTO> getAllEvents(Pageable pageable);

    EventResponseDTO getEventById(Long id);

    EventResponseDTO updateEvent(Long id, EventRequestDTO request);

    void deleteEvent(Long id);

}
