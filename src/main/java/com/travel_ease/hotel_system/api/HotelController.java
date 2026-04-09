package com.travel_ease.hotel_system.api;


import com.travel_ease.hotel_system.dto.request.HotelRequestDto;
import com.travel_ease.hotel_system.service.HotelService;
import com.travel_ease.hotel_system.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel-service/api/v1/hotels")
public class HotelController {
    private final HotelService hotelService;

    @PostMapping("/user/create")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> create(
            @RequestBody HotelRequestDto dto) {
        hotelService.create(dto);
        return new ResponseEntity<>(
                new StandardResponseDto(
                    201, "Hotel Saved!",null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> update(
            @PathVariable("id") String hotelId,
            @RequestBody HotelRequestDto dto) throws SQLException {
        hotelService.update(dto, hotelId);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201, "Hotel Updated!",null
                ),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/host/delete/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> delete(
            @PathVariable("id") String hotelId) throws SQLException {
        hotelService.delete(hotelId);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        204, "Hotel deleted!",null
                ),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/visitors/find-by-id/{id}")
    public ResponseEntity<StandardResponseDto> findById(
            @PathVariable("id") String hotelId) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Hotel found!",hotelService.findById(hotelId)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/visitors/find-all")
    public ResponseEntity<StandardResponseDto> findAll(
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Hotel list!",hotelService.findAll(page,size,searchText)
                ),
                HttpStatus.OK
        );
    }
}
