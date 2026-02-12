package com.platform.controller;

import com.platform.dto.business.BusinessRequestDTO;
import com.platform.dto.business.BusinessResponseDTO;
import com.platform.entity.User;
import com.platform.service.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping
    public ResponseEntity<Page<BusinessResponseDTO>> listBusinesses(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BusinessResponseDTO> businesses = businessService.listBusinesses(city, minRating, PageRequest.of(page, size));
        return ResponseEntity.ok(businesses);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BusinessResponseDTO> getBySlug(@PathVariable String slug) {
        BusinessResponseDTO business = businessService.getBusinessBySlug(slug);
        return ResponseEntity.ok(business);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponseDTO> getById(@PathVariable UUID id) {
        BusinessResponseDTO business = businessService.getBusinessById(id);
        return ResponseEntity.ok(business);
    }

    @PostMapping
    public ResponseEntity<BusinessResponseDTO> createBusiness(
            @Valid @RequestBody BusinessRequestDTO request) {
        BusinessResponseDTO business = businessService.createBusiness(request);
        return new ResponseEntity<>(business, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponseDTO> updateBusiness(
            @PathVariable UUID id,
            @Valid @RequestBody BusinessRequestDTO request) {
        BusinessResponseDTO business = businessService.updateBusiness(id, request);
        return ResponseEntity.ok(business);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusiness(
            @PathVariable UUID id,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        businessService.deleteBusiness(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/my-businesses")
    public ResponseEntity<List<BusinessResponseDTO>> getUserBusinesses(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<BusinessResponseDTO> businesses = businessService.getUserBusinesses(currentUser.getId());
        return ResponseEntity.ok(businesses);
    }
}
