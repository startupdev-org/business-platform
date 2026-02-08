package com.platform.controller;

import com.platform.dto.service.ServiceRequestDTO;
import com.platform.dto.service.ServiceResponseDTO;
import com.platform.entity.User;
import com.platform.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/business/{businessId}/service")
@RequiredArgsConstructor
@Tag(name = "Service", description = "Service management endpoints")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create service")
    public ResponseEntity<ServiceResponseDTO> createService(
            @PathVariable UUID businessId,
            @Valid @RequestBody ServiceRequestDTO request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        ServiceResponseDTO service = serviceService.createService(businessId, request, currentUser);
        return new ResponseEntity<>(service, HttpStatus.CREATED);
    }

    @GetMapping("/{serviceId}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<ServiceResponseDTO> getService(@PathVariable UUID serviceId) {
        ServiceResponseDTO service = serviceService.getService(serviceId);
        return ResponseEntity.ok(service);
    }

    @GetMapping
    @Operation(summary = "List business services")
    public ResponseEntity<List<ServiceResponseDTO>> listServices(@PathVariable UUID businessId) {
        List<ServiceResponseDTO> services = serviceService.getBusinessServices(businessId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/active")
    @Operation(summary = "List active services")
    public ResponseEntity<List<ServiceResponseDTO>> listActiveServices(@PathVariable UUID businessId) {
        List<ServiceResponseDTO> services = serviceService.getActiveServices(businessId);
        return ResponseEntity.ok(services);
    }

    @PutMapping("/{serviceId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update service")
    public ResponseEntity<ServiceResponseDTO> updateService(
            @PathVariable UUID businessId,
            @PathVariable UUID serviceId,
            @Valid @RequestBody ServiceRequestDTO request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        ServiceResponseDTO service = serviceService.updateService(businessId, serviceId, request, currentUser);
        return ResponseEntity.ok(service);
    }

    @DeleteMapping("/{serviceId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete service")
    public ResponseEntity<Void> deleteService(
            @PathVariable UUID businessId,
            @PathVariable UUID serviceId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        serviceService.deleteService(businessId, serviceId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
