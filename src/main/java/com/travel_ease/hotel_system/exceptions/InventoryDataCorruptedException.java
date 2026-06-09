package com.travel_ease.hotel_system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InventoryDataCorruptedException extends RuntimeException {

    public InventoryDataCorruptedException(String message) {
        super(message);
    }
}