package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.FacilityRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseFacilityDto;
import com.travel_ease.hotel_system.dto.response.paginate.FacilityPaginateResponseDto;

public interface FacilityService {
    public void create(FacilityRequestDto dto);
    public void update(FacilityRequestDto dto, String facilityId);
    public void delete(String facilityId);
    public ResponseFacilityDto findById(String facilityId);
    public FacilityPaginateResponseDto findAll(int page, int size, String roomId);
}
