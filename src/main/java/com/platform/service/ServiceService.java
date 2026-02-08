package com.platform.service;

import com.platform.dto.service.ServiceRequestDTO;
import com.platform.dto.service.ServiceResponseDTO;
import com.platform.entity.Business;
import com.platform.entity.ProvidedService;
import com.platform.entity.User;
import com.platform.exception.BusinessException;
import com.platform.exception.ResourceNotFoundException;
import com.platform.repository.BusinessRepository;
import com.platform.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final BusinessRepository businessRepository;

    @Transactional
    public ServiceResponseDTO createService(UUID businessId, ServiceRequestDTO dto, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        ProvidedService providedService = ProvidedService.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .active(true)
                .business(business)
                .build();

        providedService = serviceRepository.save(providedService);
        return toDTO(providedService);
    }

    public ServiceResponseDTO getService(UUID id) {
        ProvidedService providedService = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        return toDTO(providedService);
    }

    public List<ServiceResponseDTO> getBusinessServices(UUID businessId) {
        return serviceRepository.findByBusinessId(businessId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceResponseDTO> getActiveServices(UUID businessId) {
        return serviceRepository.findByBusinessIdAndActive(businessId, true)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceResponseDTO updateService(UUID businessId, UUID serviceId, ServiceRequestDTO dto, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        ProvidedService providedService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        providedService.setName(dto.getName());
        providedService.setDescription(dto.getDescription());
        providedService.setPrice(dto.getPrice());
        providedService.setDurationMinutes(dto.getDurationMinutes());
        if (dto.getActive() != null) {
            providedService.setActive(dto.getActive());
        }

        providedService = serviceRepository.save(providedService);
        return toDTO(providedService);
    }

    @Transactional
    public void deleteService(UUID businessId, UUID serviceId, User currentUser) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        validateBusinessOwnership(business, currentUser);

        ProvidedService providedService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        serviceRepository.delete(providedService);
    }

    private void validateBusinessOwnership(Business business, User currentUser) {
        if (!business.getOwner().getId().equals(currentUser.getId()) &&
            !currentUser.getRole().equals(User.UserRole.PLATFORM_ADMIN)) {
            throw new BusinessException("Unauthorized");
        }
    }

    private ServiceResponseDTO toDTO(ProvidedService providedService) {
        return ServiceResponseDTO.builder()
                .id(providedService.getId())
                .name(providedService.getName())
                .description(providedService.getDescription())
                .price(providedService.getPrice())
                .durationMinutes(providedService.getDurationMinutes())
                .businessId(providedService.getBusiness().getId())
                .active(providedService.getActive())
                .createdAt(providedService.getCreatedAt())
                .updatedAt(providedService.getUpdatedAt())
                .build();
    }
}
