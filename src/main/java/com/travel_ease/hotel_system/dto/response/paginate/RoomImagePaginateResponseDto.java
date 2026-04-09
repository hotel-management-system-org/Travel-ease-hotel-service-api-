package com.travel_ease.hotel_system.dto.response.paginate;


import com.travel_ease.hotel_system.dto.response.ResponseRoomImageDto;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomImagePaginateResponseDto {
    private List<ResponseRoomImageDto> dataList;
    private long dataCount;
}
