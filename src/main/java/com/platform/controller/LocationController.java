package com.platform.controller;

import com.platform.dto.location.LocationRequestDTO;
import com.platform.dto.location.LocationResponseDTO;
import com.platform.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    // Create a new location
    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody LocationRequestDTO requestDTO) {
        LocationResponseDTO responseDTO = locationService.createLocation(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Get all locations
    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        List<LocationResponseDTO> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    // Get a location by ID
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable UUID id) {
        LocationResponseDTO location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    // Update a location
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(
            @PathVariable UUID id,
            @RequestBody LocationRequestDTO requestDTO) {
        LocationResponseDTO updatedLocation = locationService.updateLocation(id, requestDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    // Delete a location
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
