package com.travel_ease.hotel_system.api;

import com.travel_ease.hotel_system.dto.request.PoliciesRequestDto;
import com.travel_ease.hotel_system.service.PoliciesService;
import com.travel_ease.hotel_system.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel-service/api/v1/hotels/policy")
public class PoliciesController {
    private final PoliciesService policiesService;

    @PostMapping("/admin/create")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> create(
            @RequestBody PoliciesRequestDto dto) {
        policiesService.create(dto);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201, "Room Saved!", null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> update(
            @PathVariable("id") long policyId,
            @RequestBody PoliciesRequestDto dto) throws SQLException {
        policiesService.update(dto, policyId);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201, "Room Updated!", null
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/visitors/find-by-id/{id}")
    public ResponseEntity<StandardResponseDto> findById(
            @PathVariable("id") String hotelId) throws SQLException {
        System.out.println("come here");
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Room found!", policiesService.findById(hotelId)
                ),
                HttpStatus.OK
        );
    }

}