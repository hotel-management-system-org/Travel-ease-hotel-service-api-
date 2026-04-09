package com.travel_ease.hotel_system.dto.response.paginate;

import com.travel_ease.hotel_system.dto.response.ResponseRoomDto;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomPaginateResponseDto {
    private List<ResponseRoomDto> dataList;
    private long dataCount;
}
