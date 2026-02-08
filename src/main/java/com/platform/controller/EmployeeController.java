package com.platform.controller;

import com.platform.dto.employee.EmployeeRequestDTO;
import com.platform.dto.employee.EmployeeResponseDTO;
import com.platform.entity.User;
import com.platform.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/business/{businessId}/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @PathVariable UUID businessId,
            @Valid @RequestBody EmployeeRequestDTO request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        EmployeeResponseDTO employee = employeeService.createEmployee(businessId, request, currentUser);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable UUID employeeId) {
        EmployeeResponseDTO employee = employeeService.getEmployee(employeeId);
        return ResponseEntity.ok(employee);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> listEmployees(@PathVariable UUID businessId) {
        List<EmployeeResponseDTO> employees = employeeService.getBusinessEmployees(businessId);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/active")
    public ResponseEntity<List<EmployeeResponseDTO>> listActiveEmployees(@PathVariable UUID businessId) {
        List<EmployeeResponseDTO> employees = employeeService.getActiveEmployees(businessId);
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable UUID businessId,
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeRequestDTO request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        EmployeeResponseDTO employee = employeeService.updateEmployee(businessId, employeeId, request, currentUser);
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable UUID businessId,
            @PathVariable UUID employeeId,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        employeeService.deleteEmployee(businessId, employeeId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
