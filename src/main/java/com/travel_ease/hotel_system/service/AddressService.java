package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.AddressRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseAddressDto;

public interface AddressService {
    public void create(AddressRequestDto dto);
    public void update(AddressRequestDto dto, String addressId);
    public void delete(String addressId);
    public ResponseAddressDto findById(String addressId);
    public ResponseAddressDto findByBranchId(String branchId);
}
