package com.travel_ease.hotel_system.service.impl;

import com.travel_ease.hotel_system.dto.request.PoliciesRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponsePoliciesDto;
import com.travel_ease.hotel_system.entity.Hotel;
import com.travel_ease.hotel_system.entity.Policies;
import com.travel_ease.hotel_system.entity.Room;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.reposiroty.HotelRepository;
import com.travel_ease.hotel_system.reposiroty.PoliciesRepository;
import com.travel_ease.hotel_system.reposiroty.RoomRepository;
import com.travel_ease.hotel_system.service.PoliciesService;
import com.travel_ease.hotel_system.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PoliciesServiceImpl implements PoliciesService {

    private final PoliciesRepository policiesRepository;
    private final Mapper mapper;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Override
    public void create(PoliciesRequestDto dto) {
        Room selectedRoom = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new EntryNotFoundException("Room Not Found"));

        Policies policy = Policies.builder()
                .policy(dto.getPolicies())
                .description(dto.getDescription())
                .room(selectedRoom)
                .build();

        policiesRepository.save(policy);
    }

    @Override
    public void update(PoliciesRequestDto dto,long policyId) {
        Room selectedRoom = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new EntryNotFoundException("Room Not Found"));

        Policies selectedPolicy = policiesRepository.findById(policyId)
                .orElseThrow(() -> new EntryNotFoundException("Policy Not Found"));

        selectedPolicy.setPolicy(dto.getPolicies());
        selectedPolicy.setDescription(dto.getDescription());
        selectedPolicy.setRoom(selectedRoom);

        policiesRepository.save(selectedPolicy);
    }

    @Override
    public List<ResponsePoliciesDto> findById(String hotelId) {
        Hotel selectedHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntryNotFoundException("Hotel Not Found"));
        List<Room> selectedRooms = roomRepository.getRoomsByHotel(selectedHotel.getHotelId())
                .orElseThrow(() -> new EntryNotFoundException("Rooms Not Found"));
        List<Policies> selectedPolicies = new ArrayList<>();

        for (Room e : selectedRooms) {
            selectedPolicies.addAll(e.getPolicies());
        }

        System.out.println("List " +  selectedPolicies.size());
        List<ResponsePoliciesDto> list = selectedPolicies
                .stream()
                .map(this::toResponsePoliciesDto)
                .toList();

        return list;

    }

    private ResponsePoliciesDto toResponsePoliciesDto(Policies policies) {
        return policies == null ? null : ResponsePoliciesDto.builder()
                .id(policies.getId())
                .description(policies.getDescription())
                .policy(policies.getPolicy())
                .roomId(policies.getRoom().getRoomId())
                .build();
    }
}
