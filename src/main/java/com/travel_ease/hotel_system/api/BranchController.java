package com.travel_ease.hotel_system.api;


import com.travel_ease.hotel_system.dto.request.BranchRequestDto;
import com.travel_ease.hotel_system.service.BranchService;
import com.travel_ease.hotel_system.util.StandardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotel-management/api/v1/branches")
public class BranchController {
    private final BranchService branchService;

    @PostMapping("/user/create")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> create(
            @RequestBody BranchRequestDto dto) {
        branchService.create(dto);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201, "Branch Saved!", null
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> update(
            @PathVariable("id") String branchId,
            @RequestBody BranchRequestDto dto) throws SQLException {
        branchService.update(dto, branchId);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        201, "Branch Updated!", null
                ),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/host/delete/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponseDto> delete(
            @PathVariable("id") String branchId) throws SQLException {
        branchService.delete(branchId);
        return new ResponseEntity<>(
                new StandardResponseDto(
                        204, "Branch deleted!", null
                ),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/visitor/find-by-id/{id}")
    public ResponseEntity<StandardResponseDto> findById(
            @PathVariable("id") String branchId) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Branch found!", branchService.findById(branchId)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/visitor/find-all")
    public ResponseEntity<StandardResponseDto> findAll(
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Branch list!", branchService.findAll(page, size, searchText)
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/visitor/find-all-by-hotel/{hotelId}")
    public ResponseEntity<StandardResponseDto> findAllByHotelId(
            @PathVariable("hotelId") String hotelId,
            @RequestParam String searchText,
            @RequestParam int page,
            @RequestParam int size
    ) throws SQLException {
        return new ResponseEntity<>(
                new StandardResponseDto(
                        200, "Branch list by hotel!", branchService.findAllByHotelId(page, size, hotelId, searchText)
                ),
                HttpStatus.OK
        );
    }
}