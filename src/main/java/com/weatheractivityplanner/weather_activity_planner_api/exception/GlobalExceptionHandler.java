package com.weatheractivityplanner.weather_activity_planner_api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.weatheractivityplanner.weather_activity_planner_api.dto.ErrorResponseDTO;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException exception) {

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException exception) {

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Validation failed");
        error.setTimestamp(LocalDateTime.now());
        error.setErrors(errors);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(WeatherProviderException.class)
    public ResponseEntity<ErrorResponseDTO> handleWeatherProviderException(
            WeatherProviderException exception) {

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        error.setMessage(exception.getMessage());
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception exception) {

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("Internal server error");
        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
