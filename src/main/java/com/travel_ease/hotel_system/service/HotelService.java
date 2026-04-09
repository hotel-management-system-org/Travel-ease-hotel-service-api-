package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.HotelRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseHotelDto;
import com.travel_ease.hotel_system.dto.response.paginate.HotelPaginateResponseDto;

import java.sql.SQLException;

public interface HotelService {
    public void create(HotelRequestDto dto);
    public void update(HotelRequestDto dto,String id) throws SQLException;
    public void delete(String id);
    public ResponseHotelDto findById(String id) throws SQLException;
    public HotelPaginateResponseDto findAll(int page, int size, String searchText);
}
