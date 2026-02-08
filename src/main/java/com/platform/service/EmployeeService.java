package com.platform.service;

import com.platform.dto.employee.EmployeeRequestDTO;
import com.platform.dto.employee.EmployeeResponseDTO;
import com.platform.entity.Business;
import com.platform.entity.Employee;
import com.platform.entity.User;
import com.platform.exception.BusinessException;
import com.platform.exception.ResourceNotFoundException;
import com.platform.repository.BusinessRepository;
import com.platform.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BusinessRepository businessRepository;

    @Transactional
    public EmployeeResponseDTO createEmployee(UUID businessId, EmployeeRequestDTO dto, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        Employee employee = Employee.builder()
                .name(dto.getName())
                .photoUrl(dto.getPhotoUrl())
                .active(true)
                .business(business)
                .build();

        employee = employeeRepository.save(employee);
        return toDTO(employee);
    }

    public EmployeeResponseDTO getEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return toDTO(employee);
    }

    public List<EmployeeResponseDTO> getBusinessEmployees(UUID businessId) {
        return employeeRepository.findByBusinessId(businessId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponseDTO> getActiveEmployees(UUID businessId) {
        return employeeRepository.findByBusinessIdAndActive(businessId, true)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID businessId, UUID employeeId, EmployeeRequestDTO dto, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setName(dto.getName());
        employee.setPhotoUrl(dto.getPhotoUrl());
        if (dto.getActive() != null) {
            employee.setActive(dto.getActive());
        }

        employee = employeeRepository.save(employee);
        return toDTO(employee);
    }

    @Transactional
    public void deleteEmployee(UUID businessId, UUID employeeId, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employeeRepository.delete(employee);
    }

    private void validateBusinessOwnership(Business business, User currentUser) {
        if (!business.getOwner().getId().equals(currentUser.getId()) &&
            !currentUser.getRole().equals(User.UserRole.PLATFORM_ADMIN)) {
            throw new BusinessException("Unauthorized");
        }
    }

    private EmployeeResponseDTO toDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .photoUrl(employee.getPhotoUrl())
                .businessId(employee.getBusiness().getId())
                .active(employee.getActive())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
