package com.travel_ease.hotel_system.dto.response.paginate;

import com.travel_ease.hotel_system.dto.response.ResponseHotelDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelPaginateResponseDto {
    private List<ResponseHotelDto> dataList;
    private long dataCount;
}
