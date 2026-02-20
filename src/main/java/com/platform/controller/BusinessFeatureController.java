package com.platform.controller;

import com.platform.dto.business.BusinessFeatureDTO;
import com.platform.entity.Business;
import com.platform.entity.BusinessFeature;
import com.platform.entity.User;
import com.platform.exception.BusinessOwnershipException;
import com.platform.exception.UserNotEnabledException;
import com.platform.repository.BusinessFeatureRepository;
import com.platform.repository.BusinessRepository;
import com.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.*;

@RestController
@RequestMapping("/api/business/{businessId}/features")
@RequiredArgsConstructor
public class BusinessFeatureController {

    private final BusinessRepository businessRepository;
    private final BusinessFeatureRepository featureRepository;


    private final UserService userService;

    // GET all features for a business
    @GetMapping
    public ResponseEntity<List<BusinessFeatureDTO>> getAll(@PathVariable UUID businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        List<BusinessFeatureDTO> dtoList = business.getFeatures()
                .stream()
                .map(f -> BusinessFeatureDTO.builder()
                        .id(f.getId())
                        .name(f.getName())
                        .build())
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    // POST - add a feature
    @PostMapping
    public ResponseEntity<BusinessFeatureDTO> addFeature(
            @PathVariable UUID businessId,
            @RequestBody BusinessFeatureDTO request) {

        User user = userService.getUser();

        if (!user.getIsEnabled())
            throw new UserNotEnabledException("User is not enabled");

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        if (business.isNotOwner(user)) {
            throw new BusinessOwnershipException("Cannot add a new feature to a business you do not own");
        }

        BusinessFeature feature = BusinessFeature.builder()
                .business(business)
                .name(request.getName())
                .build();

        business.getFeatures().add(feature);
        businessRepository.save(business);

        return ResponseEntity.ok(BusinessFeatureDTO.builder()
                .id(feature.getId())
                .name(feature.getName())
                .build());
    }

    // DELETE - remove a feature
    @DeleteMapping("/{featureId}")
    public ResponseEntity<Void> removeFeature(
            @PathVariable UUID businessId,
            @PathVariable Long featureId) {

        BusinessFeature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new RuntimeException("Feature not found"));

        featureRepository.delete(feature);

        return ResponseEntity.noContent().build();
    }
}
