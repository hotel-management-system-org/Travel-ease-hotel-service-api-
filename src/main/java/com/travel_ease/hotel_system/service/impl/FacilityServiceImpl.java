package com.travel_ease.hotel_system.service.impl;

import com.travel_ease.hotel_system.dto.request.FacilityRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseFacilityDto;
import com.travel_ease.hotel_system.dto.response.paginate.FacilityPaginateResponseDto;
import com.travel_ease.hotel_system.entity.Facility;
import com.travel_ease.hotel_system.entity.Room;
import com.travel_ease.hotel_system.exceptions.AlreadyExistsException;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.reposiroty.FacilityRepository;
import com.travel_ease.hotel_system.reposiroty.RoomRepository;
import com.travel_ease.hotel_system.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final RoomRepository roomRepository;

    @Override
    public void create(FacilityRequestDto dto) {

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Room not found with id: %s", dto.getRoomId())
                ));


        if (facilityRepository.existsByNameAndRoom(dto.getName(), room)) {
            throw new RuntimeException(
                    String.format("Facility '%s' already exists for this room", dto.getName())
            );
        }

        Facility facility = Facility.builder()
                .name(dto.getName())
                .room(room)
                .build();

        facilityRepository.save(facility);
    }

    @Override
    public void update(FacilityRequestDto dto, String facilityId) {

        long id;
        try {
            id = Long.parseLong(facilityId);
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("Invalid facility id format: %s", facilityId)
            );
        }

        Facility selectedFacility = facilityRepository.findById(id)
                .orElseThrow(() -> new EntryNotFoundException("Facility not found with id: " + facilityId));

        if(dto.getRoomId() != null && !dto.getRoomId().equals(selectedFacility.getRoom().getRoomId())){
            Room selectedRoom = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new EntryNotFoundException("Room not found with id: " + dto.getRoomId()));

            if(facilityRepository.existsByNameAndRoom(dto.getName(), selectedRoom)){
                throw new AlreadyExistsException("Facility already exists for this room");
            }
            selectedFacility.setRoom(selectedRoom);
        }else {
            if(!dto.getName().equals(selectedFacility.getName())
                    && facilityRepository.existsByNameAndRoom(dto.getName(), selectedFacility.getRoom())){
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
