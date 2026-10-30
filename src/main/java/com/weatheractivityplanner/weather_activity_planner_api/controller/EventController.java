package com.weatheractivityplanner.weather_activity_planner_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventRequestDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO eventRequestDTO) {
        EventResponseDTO eventResponseDTO = eventService.createEvent(eventRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(eventResponseDTO);
    }

    @GetMapping
    public Page<EventResponseDTO> getAllEvents(Pageable pageable) {
        return eventService.getAllEvents(pageable);
    }

    @GetMapping("/{id}")
    public EventResponseDTO getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/{id}")
    public EventResponseDTO updateEvent(@PathVariable Long id,
            @RequestBody @Valid EventRequestDTO eventRequestDTO) {
        return eventService.updateEvent(id, eventRequestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        
        eventService.deleteEvent(id);

        return ResponseEntity.noContent().build();
    }

}
