package com.travel_ease.hotel_system.api.internal;

import com.travel_ease.hotel_system.dto.request.ConfirmBookingRequestDto;
import com.travel_ease.hotel_system.dto.request.internal.HoldRoomRequestDto;
import com.travel_ease.hotel_system.dto.request.internal.HotelBookingValidationResponse;
import com.travel_ease.hotel_system.service.RoomService;
import com.travel_ease.hotel_system.service.inrernal.InternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RestController
@RequestMapping("/hotel-service/api/v1/rooms/internal")
@RequiredArgsConstructor
@Slf4j
public class InternalController {
    private final InternalService holdService;
    private final RoomService roomService;

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
    public ResponseEntity<Void> releaseHold(@Valid  @RequestBody HoldRoomRequestDto dto){

        boolean released = holdService.releaseHold(dto);
        if (released) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/update-inventory")
    public ResponseEntity<Boolean> updateInventory(@RequestBody ConfirmBookingRequestDto dto) {
          log.info("Received internal request to update inventory for Room: {}", dto.roomId());
        boolean isUpdate = roomService.updateInventoryOnConfirmation(dto);
        if (isUpdate) {
           return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(false);
        }
    }


}
