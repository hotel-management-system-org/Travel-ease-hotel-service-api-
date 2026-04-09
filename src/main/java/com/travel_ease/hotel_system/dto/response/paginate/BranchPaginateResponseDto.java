package com.travel_ease.hotel_system.dto.response.paginate;

import com.travel_ease.hotel_system.dto.response.ResponseBranchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchPaginateResponseDto {
    private List<ResponseBranchDto> dataList;
    private long dataCount;
}
