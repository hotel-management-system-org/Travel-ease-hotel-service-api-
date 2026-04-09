package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.RoomRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseRoomDto;
import com.travel_ease.hotel_system.dto.response.paginate.RoomPaginateResponseDto;

public interface RoomService {
    public void create(RoomRequestDto dto);
    public void update(RoomRequestDto dto, String roomId);
    public void delete(String roomId);
    public ResponseRoomDto findById(String roomId);
    public RoomPaginateResponseDto findAll(int page, int size);
}
