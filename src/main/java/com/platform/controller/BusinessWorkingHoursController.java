package com.platform.controller;

import com.platform.dto.business.CreateWorkingHoursRequest;
import com.platform.dto.business.BusinessWorkingHoursDTO;
import com.platform.service.BusinessWorkingHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/businesses/{businessId}/working-hours")
@RequiredArgsConstructor
public class BusinessWorkingHoursController {

    private final BusinessWorkingHoursService service;

    @PostMapping
    public ResponseEntity<BusinessWorkingHoursDTO> create(
            @PathVariable UUID businessId,
            @RequestBody CreateWorkingHoursRequest request) {

        return ResponseEntity.ok(
                service.create(businessId, request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable UUID businessId) {
        return ResponseEntity.ok(
                service.getByBusiness(businessId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable UUID businessId,
            @PathVariable Long id) {

        service.delete(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
