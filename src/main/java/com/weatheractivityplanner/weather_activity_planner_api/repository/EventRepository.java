package com.weatheractivityplanner.weather_activity_planner_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatheractivityplanner.weather_activity_planner_api.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
