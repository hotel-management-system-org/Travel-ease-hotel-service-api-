package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.HotelFacilityRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseFacilityDto;
import com.travel_ease.hotel_system.dto.response.paginate.FacilityPaginateResponseDto;

public interface HotelFacilityService {
    public void create(HotelFacilityRequestDto dto);
    public void update(HotelFacilityRequestDto dto, String facilityId);
    public void delete(String facilityId);
    public ResponseFacilityDto findById(String facilityId);
    public FacilityPaginateResponseDto findAll(int page, int size, String roomId);
}
