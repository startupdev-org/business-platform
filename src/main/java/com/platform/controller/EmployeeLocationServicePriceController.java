package com.platform.controller;

import com.platform.dto.EmployeeLocationServicePriceRequestDTO;
import com.platform.dto.EmployeeLocationServicePriceResponseDTO;
import com.platform.service.EmployeeLocationServicePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee-service-price")
@RequiredArgsConstructor
public class EmployeeLocationServicePriceController {

    private final EmployeeLocationServicePriceService priceService;

    @PostMapping
    public ResponseEntity<EmployeeLocationServicePriceResponseDTO> create(
            @RequestBody EmployeeLocationServicePriceRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeLocationServicePriceResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody EmployeeLocationServicePriceRequestDTO dto) {
        return ResponseEntity.ok(priceService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeLocationServicePriceResponseDTO> getById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(priceService.getById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeLocationServicePriceResponseDTO>> getByEmployee(
            @PathVariable UUID employeeId) {
        return ResponseEntity.ok(priceService.getByEmployee(employeeId));
    }

    @GetMapping("/employee/{employeeId}/location/{locationId}")
    public ResponseEntity<List<EmployeeLocationServicePriceResponseDTO>> getByEmployeeAndLocation(
            @PathVariable UUID employeeId,
            @PathVariable UUID locationId) {
        return ResponseEntity.ok(priceService.getByEmployeeAndLocation(employeeId, locationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        priceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}