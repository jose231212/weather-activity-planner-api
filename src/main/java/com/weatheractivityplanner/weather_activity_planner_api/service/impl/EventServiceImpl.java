package com.weatheractivityplanner.weather_activity_planner_api.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventRequestDTO;
import com.weatheractivityplanner.weather_activity_planner_api.dto.event.EventResponseDTO;
import com.weatheractivityplanner.weather_activity_planner_api.entity.Event;
import com.weatheractivityplanner.weather_activity_planner_api.exception.ResourceNotFoundException;
import com.weatheractivityplanner.weather_activity_planner_api.repository.EventRepository;
import com.weatheractivityplanner.weather_activity_planner_api.service.EventService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;

    @Override
    public EventResponseDTO createEvent(EventRequestDTO request) {
        
        validateTemperatueRange(request.getMinTemperature(), request.getMaxTemperature());

        Event event = new Event();
        event.setName(request.getName());
        event.setActivityType(request.getActivityType());
        event.setCity(request.getCity());
        event.setCountry(request.getCountry());
        event.setLatitude(request.getLatitude());
        event.setLongitude(request.getLongitude());
        event.setScheduledAt(request.getScheduledAt());
        event.setMinTemperature(request.getMinTemperature());
        event.setMaxTemperature(request.getMaxTemperature());
        event.setMaxPrecipitationProbability(request.getMaxPrecipitationProbability());
        event.setMaxWindSpeed(request.getMaxWindSpeed());
        event.setReschedulable(request.isReschedulable());

        Event savedEvent = eventRepository.save(event);

        return mapToResponseDTO(savedEvent);

    }

    @Override
    public Page<EventResponseDTO> getAllEvents(Pageable pageable) {
        
        Page<Event> events = eventRepository.findAll(pageable);

        return events.map(this::mapToResponseDTO);

    }

    @Override
    public EventResponseDTO getEventById(Long id) {
        
        Event event = eventRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "Event not found with id: " + id
                        ));


        return mapToResponseDTO(event);

    }

    @Override
    public EventResponseDTO updateEvent(Long id, EventRequestDTO request) {

        validateTemperatueRange(request.getMinTemperature(), request.getMaxTemperature());

        Event event = eventRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "Event not found with id: " + id
                        ));

        event.setName(request.getName());
        event.setActivityType(request.getActivityType());
        event.setCity(request.getCity());
        event.setCountry(request.getCountry());
        event.setLatitude(request.getLatitude());
        event.setLongitude(request.getLongitude());
        event.setScheduledAt(request.getScheduledAt());
        event.setMinTemperature(request.getMinTemperature());
        event.setMaxTemperature(request.getMaxTemperature());
        event.setMaxPrecipitationProbability(request.getMaxPrecipitationProbability());
        event.setMaxWindSpeed(request.getMaxWindSpeed());
        event.setReschedulable(request.isReschedulable());

        Event savedEvent = eventRepository.save(event);

        return mapToResponseDTO(savedEvent);

    }

    @Override
    public void deleteEvent(Long id) {
        
        Event event = eventRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "Event not found with id: " + id
                        ));

        eventRepository.delete(event);

    }

    private EventResponseDTO mapToResponseDTO(Event event) {

        EventResponseDTO responseDTO = new EventResponseDTO();
        responseDTO.setId(event.getId());
        responseDTO.setName(event.getName());
        responseDTO.setActivityType(event.getActivityType());
        responseDTO.setCity(event.getCity());
        responseDTO.setCountry(event.getCountry());
        responseDTO.setLongitude(event.getLongitude());
        responseDTO.setLatitude(event.getLatitude());
        responseDTO.setScheduledAt(event.getScheduledAt());
        responseDTO.setMinTemperature(event.getMinTemperature());
        responseDTO.setMaxTemperature(event.getMaxTemperature());
        responseDTO.setMaxPrecipitationProbability(event.getMaxPrecipitationProbability());
        responseDTO.setMaxWindSpeed(event.getMaxWindSpeed());
        responseDTO.setReschedulable(event.isReschedulable());

        return  responseDTO;

    }

    private void validateTemperatueRange(Double minTemperature, Double maxTemperature) {

        if (minTemperature > maxTemperature) {
            throw new IllegalArgumentException("Minimum temperature cannot be greater than maximum temperature");
        }

    }

}
