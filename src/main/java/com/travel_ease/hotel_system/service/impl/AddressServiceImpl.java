package com.travel_ease.hotel_system.service.impl;

import com.travel_ease.hotel_system.dto.request.AddressRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseAddressDto;
import com.travel_ease.hotel_system.entity.Address;
import com.travel_ease.hotel_system.entity.Branch;
import com.travel_ease.hotel_system.exceptions.AlreadyExistsException;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.reposiroty.AddressRepository;
import com.travel_ease.hotel_system.reposiroty.BranchRepository;
import com.travel_ease.hotel_system.service.AddressService;
import com.travel_ease.hotel_system.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final BranchRepository branchRepository;
    private final Mapper mapper;

    @Override
    public void create(AddressRequestDto dto) {
        Branch selectedBranch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new EntryNotFoundException("Branch not found"));

        if(addressRepository.existsByBranch(selectedBranch)){
            throw new AlreadyExistsException("Branch already exists");
        }

        Address address = Address.builder()
                .addressId(UUID.randomUUID().toString())
                .addressLine(dto.getAddressLine())
                .city(dto.getCity())
                .country(dto.getCountry())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .branch(selectedBranch)
                .build();

        addressRepository.save(address);
    }

    @Override
    public void update(AddressRequestDto dto, String addressId) {
        Address selectedAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntryNotFoundException("Address not found"));

        if(dto.getBranchId() != null && !selectedAddress.getBranch().getBranchId().equals(dto.getBranchId())) {
            Branch newBranch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new EntryNotFoundException("Branch not found"));

            if(addressRepository.existsByBranch(newBranch)){
                throw new AlreadyExistsException("Branch already exists");
            }

            selectedAddress.setBranch(newBranch);

        }

        selectedAddress.setAddressLine(dto.getAddressLine());
        selectedAddress.setCity(dto.getCity());
        selectedAddress.setCountry(dto.getCountry());
        selectedAddress.setLatitude(dto.getLatitude());
        selectedAddress.setLongitude(dto.getLongitude());

        addressRepository.save(selectedAddress);
    }

    @Override
    public void delete(String addressId) {
        Address selectedAddress = addressRepository
                .findById(addressId).orElseThrow(() -> new EntryNotFoundException("Address not found"));

        addressRepository.delete(selectedAddress);
    }

    @Override
    public ResponseAddressDto findById(String addressId) {
        Address selectedAddress = addressRepository
                .findById(addressId).orElseThrow(() -> new EntryNotFoundException("Address not found"));

       return (ResponseAddressDto) mapper.toResponseDto(selectedAddress);
    }

    @Override
    public ResponseAddressDto findByBranchId(String branchId) {
        Branch selectedBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntryNotFoundException("Branch not found"));

        return (ResponseAddressDto) mapper.toResponseDto(selectedBranch);
    }
}
