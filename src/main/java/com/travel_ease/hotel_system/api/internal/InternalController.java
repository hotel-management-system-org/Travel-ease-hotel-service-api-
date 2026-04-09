package com.travel_ease.hotel_system.api.internal;

import com.travel_ease.hotel_system.dto.request.internal.HoldRoomRequestDto;
import com.travel_ease.hotel_system.dto.request.internal.HotelBookingValidationResponse;
import com.travel_ease.hotel_system.service.inrernal.InternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RestController
@RequestMapping("/hotel-service/api/v1/rooms/internal")
@RequiredArgsConstructor
public class InternalController {
    private final InternalService holdService;

    @PostMapping("/hold")
    public ResponseEntity<Void> holdRoom(@RequestBody HoldRoomRequestDto request) {
        boolean held = holdService.holdRoom(request);
        if (held) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}/validate-booking")
    public ResponseEntity<HotelBookingValidationResponse> validateHotel(
            @PathVariable("id") String hotelId) throws SQLException {
        HotelBookingValidationResponse response = holdService.validateHotel(hotelId);

        response.setBookingAllowed(response.isStatus());

        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/hold-release")
    public ResponseEntity<Void> releaseHold(@RequestBody HoldRoomRequestDto dto) throws SQLException {
        boolean released = holdService.releaseHold(dto);
        if (released) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
