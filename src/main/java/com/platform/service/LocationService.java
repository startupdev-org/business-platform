package com.platform.service;

import com.platform.dto.location.LocationRequestDTO;
import com.platform.dto.location.LocationResponseDTO;
import com.platform.entity.Business;
import com.platform.entity.Location;
import com.platform.repository.BusinessRepository;
import com.platform.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final BusinessRepository businessRepository; // to fetch the business

    // Create a location
    public LocationResponseDTO createLocation(LocationRequestDTO dto) {
        Business business = businessRepository.findById(dto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Location location = Location.builder()
                .business(business)
                .name(dto.getName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .country(dto.getCountry())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .isDefaultLocation(dto.getIsDefaultLocation() != null ? dto.getIsDefaultLocation() : false)
                .build();

        Location saved = locationRepository.save(location);
        return mapToDTO(saved);
    }

    // Get all locations
    public List<LocationResponseDTO> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get location by ID
    public LocationResponseDTO getLocationById(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return mapToDTO(location);
    }

    // Update location
    public LocationResponseDTO updateLocation(UUID id, LocationRequestDTO dto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        if (dto.getBusinessId() != null) {
            Business business = businessRepository.findById(dto.getBusinessId())
                    .orElseThrow(() -> new RuntimeException("Business not found"));
            location.setBusiness(business);
        }

        if (dto.getName() != null) location.setName(dto.getName());
        if (dto.getAddress() != null) location.setAddress(dto.getAddress());
        if (dto.getCity() != null) location.setCity(dto.getCity());
        if (dto.getCountry() != null) location.setCountry(dto.getCountry());
        if (dto.getLatitude() != null) location.setLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) location.setLongitude(dto.getLongitude());
        if (dto.getIsDefaultLocation() != null) location.setIsDefaultLocation(dto.getIsDefaultLocation());

        Location updated = locationRepository.save(location);
        return mapToDTO(updated);
    }

    // Delete location
    public void deleteLocation(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        locationRepository.delete(location);
    }

    // Mapper
    private LocationResponseDTO mapToDTO(Location location) {
        return LocationResponseDTO.builder()
                .id(location.getId())
                .businessId(location.getBusiness() != null ? location.getBusiness().getId() : null)
                .name(location.getName())
                .address(location.getAddress())
                .city(location.getCity())
                .country(location.getCountry())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .isDefaultLocation(location.getIsDefaultLocation())
                .build();
    }
}
