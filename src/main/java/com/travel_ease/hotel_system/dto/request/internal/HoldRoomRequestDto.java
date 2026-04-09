package com.travel_ease.hotel_system.dto.request.internal;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record HoldRoomRequestDto(
        UUID roomId,
        int quantity,
        LocalDate checkIn,
        LocalDate checkOut
) {
}
