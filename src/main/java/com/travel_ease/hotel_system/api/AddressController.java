package com.travel_ease.hotel_system.api;

import com.travel_ease.hotel_system.dto.request.AddressRequestDto;
import com.travel_ease.hotel_system.service.AddressService;
import com.travel_ease.hotel_system.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel-management/api/v1/addresses")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/user/create")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> create(
            @RequestBody AddressRequestDto dto) {
        addressService.create(dto);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201, "Address Saved!", null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> update(
            @PathVariable("id") String addressId,
            @RequestBody AddressRequestDto dto) throws SQLException {
        addressService.update(dto, addressId);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201, "Address Updated!", null
                ),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/host/delete/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> delete(
            @PathVariable("id") String addressId) throws SQLException {
        addressService.delete(addressId);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        204, "Address deleted!", null
                ),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/visitor/find-by-id/{id}")
    public ResponseEntity<StandardResponseDto> findById(
            @PathVariable("id") String addressId) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Address found!", addressService.findById(addressId)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/visitor/find-by-branch/{branchId}")
    public ResponseEntity<StandardResponseDto> findByBranchId(
            @PathVariable("branchId") String branchId) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Address found by branch!", addressService.findByBranchId(branchId)
                ),
                HttpStatus.OK
        );
    }
}