package com.travel_ease.hotel_system.util;

import com.travel_ease.hotel_system.dto.request.HotelRequestDto;
import com.travel_ease.hotel_system.dto.response.*;
import com.travel_ease.hotel_system.dto.response.paginate.BranchPaginateResponseDto;
import com.travel_ease.hotel_system.dto.response.paginate.RoomImagePaginateResponseDto;
import com.travel_ease.hotel_system.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Mapper {

   private final ByteCodeHandler byteCodeHandle;
   private final FileDataExtractor fileDataExtractor;

    public Hotel toHotel(HotelRequestDto dto) throws SQLException {
        return dto==null ? null : Hotel.builder()
                .hotelId(UUID.randomUUID().toString())
                .hotelName(dto.getHotelName())
                .mainImagePath(dto.getMainImage())
                .starRating(dto.getStarRating())
                .description(byteCodeHandle.stringToBlob(dto.getDescription()))
                .activeStatus(true)
                .startingFrom(dto.getStartingFrom())
                .build();
    }

    public ResponseHotelDto toResponseHotelDto(Hotel selectedHotel) throws SQLException {
        System.out.println("selectedHotel: " + selectedHotel.getRooms());
        return ResponseHotelDto.builder()
                .hotelId(selectedHotel.getHotelId())
                .hotelName(selectedHotel.getHotelName())
                .rooms(selectedHotel.getRooms().stream().map(this::toResponseRoomDto).collect(Collectors.toList()))
                .starRating(selectedHotel.getStarRating())
                .mainImage(selectedHotel.getMainImagePath())
                .createdAt(selectedHotel.getCreatedAt())
                .updatedAt(selectedHotel.getUpdatedAt())
                .hotelFacilities(selectedHotel.getHotelFacilities().stream().map(e-> {
                    try {
                        return this.toHotelResponseDto(e);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }).collect(Collectors.toList()))
                .description(byteCodeHandle.blobToString(selectedHotel.getDescription()))
                .activeStatus(selectedHotel.isActiveStatus())
                .startingFrom(selectedHotel.getStartingFrom())
                .branches(selectedHotel.getBranches().stream().map(this::toResponseBranchDto).collect(Collectors.toList()))
                .build();
    }

    private ResponseHotelFacilityDto toHotelResponseDto(HotelFacility hotelFacility) throws SQLException {
        return ResponseHotelFacilityDto.builder()
                .id(hotelFacility.getId())
                .hotelId(hotelFacility.getHotel().getHotelId())
                .icon(hotelFacility.getIcon())
                .name(hotelFacility.getName())
                .build();
    }

    public ResponseRoomDto toResponseRoomDto(Room room){

        List<ResponseFacilityDto> facilities = Collections.emptyList();
        if (room.getFacilities() != null && !room.getFacilities().isEmpty()) {
            facilities = room.getFacilities().stream()
                    .map(this::mapFacilityToDto)
                    .collect(Collectors.toList());
        }

        List<ResponseRoomImageDto> images = Collections.emptyList();
        if (room.getRoomImages() != null && !room.getRoomImages().isEmpty()) {
            images = room.getRoomImages().stream()
                    .map(this::mapImageToDto)
                    .collect(Collectors.toList());
        }

        System.out.println("Facilities: " + facilities);
        return ResponseRoomDto.builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getType())
                .checkIn(room.getCheckIn())
                .checkOut(room.getCheckOut())
                .bedCount(room.getBedCount())
                .price(room.getPrice())
                .isAvailable(room.getStatus())
                .branchId(room.getBranch().getBranchId())
                .facilities(facilities)
                .images(images)
                .build();
    }

    public RoomImagePaginateResponseDto buildPaginateResponse(Page<RoomImage> imagePage) {
        List<ResponseRoomImageDto> dataList = imagePage.getContent().stream()
                .map(this::mapImageToDto)
                .collect(Collectors.toList());

        return RoomImagePaginateResponseDto.builder()
                .dataList(dataList)
                .dataCount(imagePage.getTotalElements())
                .build();
    }

    public BranchPaginateResponseDto toBranchPaginateResponseDto(Page<Branch> branchPage){
        List<ResponseBranchDto> responseList = branchPage.getContent()
                .stream()
                .map(this::toResponseBranchDto)
                .toList();

        return BranchPaginateResponseDto.builder()
                .dataList(responseList)
                .dataCount(branchPage.getTotalElements())
                .build();
    }

    public ResponseRoomImageDto mapImageToDto(RoomImage image) {
        return ResponseRoomImageDto.builder()
                .id(image.getId())
                .directory(fileDataExtractor.byteArrayToString(image.getFileFormatter().getDirectory()))
                .fileName(fileDataExtractor.byteArrayToString(image.getFileFormatter().getFileName()))
                .hash(fileDataExtractor.byteArrayToString(image.getFileFormatter().getHash()))
                .resourceUrl(fileDataExtractor.byteArrayToString(image.getFileFormatter().getResourceUrl()))
                .roomId(image.getRoom().getRoomId())
                .build();
    }

    private ResponseFacilityDto mapFacilityToDto(Facility facility) {
        return ResponseFacilityDto.builder()
                .id(facility.getId())
                .name(facility.getName())
                .roomId(facility.getRoom().getRoomId())
                .build();
    }



    public ResponseBranchDto toResponseBranchDto(Branch selectedBranch) {
        return ResponseBranchDto.builder()
                .branchId(selectedBranch.getBranchId())
                .branchName(selectedBranch.getBranchName())
                .branchType(selectedBranch.getBranchType())
                .roomCount(selectedBranch.getRoomCount())
                .hotelId(selectedBranch.getHotel().getHotelId())
                .address((ResponseAddressDto) toResponseDto(selectedBranch.getAddress()))
                .build();
    }

    public Object toResponseDto(Object obj) {
        System.out.println("To string " + obj.toString());
        if (obj instanceof Address address) {
            return ResponseAddressDto.builder()
                    .addressId(address.getAddressId())
                    .addressLine(address.getAddressLine())
                    .city(address.getCity())
                    .country(address.getCountry())
                    .longitude(address.getLongitude())
                    .latitude(address.getLatitude())
                    .build();
        }

        if (obj instanceof Branch branch) {
            return ResponseBranchDto.builder()
                    .branchId(branch.getBranchId())
                    .branchName(branch.getBranchName())
                    .branchType(branch.getBranchType())
                    .roomCount(branch.getRoomCount())
                    .hotelId(branch.getHotel().getHotelId())
                    .build();
        }

        throw new IllegalArgumentException("Unsupported type: " + obj.getClass());
    }

}
