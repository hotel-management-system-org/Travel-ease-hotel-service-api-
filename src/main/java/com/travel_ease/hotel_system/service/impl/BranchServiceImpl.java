package com.travel_ease.hotel_system.service.impl;

import com.travel_ease.hotel_system.dto.request.BranchRequestDto;
import com.travel_ease.hotel_system.dto.response.ResponseBranchDto;
import com.travel_ease.hotel_system.dto.response.paginate.BranchPaginateResponseDto;
import com.travel_ease.hotel_system.entity.Branch;
import com.travel_ease.hotel_system.entity.Hotel;
import com.travel_ease.hotel_system.exceptions.AlreadyExistsException;
import com.travel_ease.hotel_system.exceptions.EntryNotFoundException;
import com.travel_ease.hotel_system.reposiroty.BranchRepository;
import com.travel_ease.hotel_system.reposiroty.HotelRepository;
import com.travel_ease.hotel_system.service.BranchService;
import com.travel_ease.hotel_system.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final HotelRepository hotelRepository;
    private final Mapper mapper;


    @Override
    public void create(BranchRequestDto dto) {

        Hotel selectedHotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new EntryNotFoundException("Hotel not found"));

        if(branchRepository.existsByBranchNameAndHotel(dto.getBranchName(),selectedHotel)){
            throw new AlreadyExistsException("Branch already exists");
        }

        Branch branch = Branch.builder()
                .branchId(UUID.randomUUID().toString())
                .branchName(dto.getBranchName())
                .hotel(selectedHotel)
                .roomCount(dto.getRoomCount())
                .branchType(dto.getBranchType())
                .build();

        branchRepository.save(branch);

    }

    @Override
    public void update(BranchRequestDto dto, String branchId) {
        Branch selectedBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntryNotFoundException("Branch not found"));

        if(dto.getHotelId() != null && !dto.getHotelId().equals(selectedBranch.getHotel().getHotelId())){
            Hotel newHotel = hotelRepository.findById(dto.getHotelId())
                    .orElseThrow(() -> new EntryNotFoundException("Hotel not found"));
            selectedBranch.setHotel(newHotel);
        }

        if(!dto.getBranchName().equals(selectedBranch.getBranchName()) && branchRepository.existsByBranchNameAndHotel(dto.getBranchName(),selectedBranch.getHotel())){
            throw new AlreadyExistsException("Branch already exists");
        }

        selectedBranch.setBranchName(dto.getBranchName());
        selectedBranch.setRoomCount(dto.getRoomCount());
        selectedBranch.setBranchType(dto.getBranchType());

        branchRepository.save(selectedBranch);

    }

    @Override
    public void delete(String branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Branch not found with id: %s", branchId)
                ));

        if (branch.getRooms() != null && !branch.getRooms().isEmpty()) {
            throw new RuntimeException(
                    String.format("Cannot delete branch with id: %s. It has associated rooms.", branchId)
            );
        }

        if (branch.getAddress() != null) {
            throw new RuntimeException(
                    String.format("Cannot delete branch with id: %s. It has an associated address.", branchId)
            );
        }

        branchRepository.deleteById(branchId);
    }

    @Override
    public ResponseBranchDto findById(String branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Branch not found with id: %s", branchId)
                ));

        return mapper.toResponseBranchDto(branch);
    }

    @Override
    public BranchPaginateResponseDto findAll(int page, int size, String searchText) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Branch> branchPage;

        if(searchText == null || searchText.trim().isEmpty()){
            branchPage = branchRepository.findAll(pageable);
        }else {
            branchPage = branchRepository.findAllByBranchNameContainingIgnoreCase(searchText.trim() , pageable);
        }

        return mapper.toBranchPaginateResponseDto(branchPage);
    }

    @Override
    public BranchPaginateResponseDto findAllByHotelId(int page, int size, String hotelId, String searchText) {
        Hotel selectedHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntryNotFoundException("Hotel not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Branch> branchPage;

        if(searchText == null || searchText.trim().isEmpty()){
            branchPage = branchRepository.findAllByHotel(pageable,selectedHotel);
        }else {
            branchPage =branchRepository
                    .findAllByHotelAndBranchNameContainingIgnoreCase(selectedHotel,searchText.trim(),pageable);
        }

        return mapper.toBranchPaginateResponseDto(branchPage);
    }
}
