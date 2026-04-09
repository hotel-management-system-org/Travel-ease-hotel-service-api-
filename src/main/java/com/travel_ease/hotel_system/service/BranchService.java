package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.BranchRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseBranchDto;
import com.travel_ease.hotel_system.dto.response.paginate.BranchPaginateResponseDto;

public interface BranchService {
    public void create(BranchRequestDto dto);
    public void update(BranchRequestDto dto, String branchId);
    public void delete(String branchId);
    public ResponseBranchDto findById(String branchId);
    public BranchPaginateResponseDto findAll(int page, int size, String searchText);
    public BranchPaginateResponseDto findAllByHotelId(int page, int size,String hotelId, String searchText);
}