package com.travel_ease.hotel_system.service.impl;

import com.travel_ease.hotel_system.dto.request.HotelFacilityRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseFacilityDto;
import com.travel_ease.hotel_system.dto.response.paginate.FacilityPaginateResponseDto;
import com.travel_ease.hotel_system.entity.Hotel;
import com.travel_ease.hotel_system.entity.HotelFacility;
import com.travel_ease.hotel_system.exceptions.AlreadyExistsException;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.reposiroty.HotelFacilityRepository;
import com.travel_ease.hotel_system.reposiroty.HotelRepository;
import com.travel_ease.hotel_system.service.HotelFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelFacilityServiceImpl implements HotelFacilityService {

    private final HotelFacilityRepository facilityRepository;
    private final HotelRepository hotelRepository;

    @Override
    public void create(HotelFacilityRequestDto dto) {

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Room not found with id: %s", dto.getHotelId())
                ));


        if (facilityRepository.existsByNameAndHotel(dto.getName(),hotel)) {
            throw new RuntimeException(
                    String.format("Facility '%s' already exists for this room", dto.getName())
            );
        }

        HotelFacility facility = HotelFacility.builder()
                .name(dto.getName())
                .hotel(hotel)
                .icon(dto.getIcon())
                .build();

        facilityRepository.save(facility);
    }

    @Override
    public void update(HotelFacilityRequestDto dto, String facilityId) {

        long id;
        try {
            id = Long.parseLong(facilityId);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("Invalid facility id format: %s", facilityId)
            );
        }

        HotelFacility selectedFacility = facilityRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Facility not found with id: " + facilityId));

        if(dto.getHotelId() != null && !dto.getHotelId().equals(selectedFacility.getHotel().getHotelId())){
            Hotel selectedHotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new EntryNotFoundException("Room not found with id: " + dto.getHotelId()));

            if(facilityRepository.existsByNameAndHotel(dto.getName(), selectedHotel)){
                throw new AlreadyExistsException("Facility already exists for this room");
            }
            selectedFacility.setHotel(selectedHotel);
        }else {
            if(!dto.getName().equals(selectedFacility.getName())
                    && facilityRepository.existsByNameAndHotel(dto.getName(), selectedFacility.getHotel())){
                throw new AlreadyExistsException("Facility already exists for this room");

            }
        }

        selectedFacility.setName(dto.getName());
        facilityRepository.save(selectedFacility);

    }

    @Override
    public void delete(String facilityId) {

    }

    @Override
    public ResponseFacilityDto findById(String facilityId) {
        return null;
    }

    @Override
    public FacilityPaginateResponseDto findAll(int page, int size, String roomId) {
        return null;
    }
}
