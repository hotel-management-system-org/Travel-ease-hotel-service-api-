package com.travel_ease.hotel_system.service;

import com.travel_ease.hotel_system.dto.request.RoomImageRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseRoomImageDto;
import com.travel_ease.hotel_system.dto.response.paginate.RoomImagePaginateResponseDto;

public interface RoomImageService {
    public void create(RoomImageRequestDto dto);
    public void update(RoomImageRequestDto dto, String imageId);
    public void delete(String imageId);
    public ResponseRoomImageDto findById(String imageId);
    public RoomImagePaginateResponseDto findAll(int page, int size, String roomId);
}
