package com.travel_ease.hotel_system.adviser;


import com.travel_ease.hotel_system.exceptions.AlreadyExistsException;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.util.StandardErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardErrorResponseDto> handleEntryNotFoundException(EntryNotFoundException ex) {
        return new ResponseEntity<StandardErrorResponseDto>(
                new StandardErrorResponseDto(404,ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<StandardErrorResponseDto> handleAlreadyExistsException(AlreadyExistsException ex) {
        return new ResponseEntity<StandardErrorResponseDto>(
                new StandardErrorResponseDto(409,ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }
}
