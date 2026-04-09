package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.PoliciesRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponsePoliciesDto;

import java.util.List;

public interface PoliciesService {
    public void create(PoliciesRequestDto dto);
    public void update(PoliciesRequestDto dto, long policyId);
    public List<ResponsePoliciesDto> findById(String hotelId);
}
