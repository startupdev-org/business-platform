package com.platform.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record EmployeeLocationServicePriceResponseDTO(
        UUID id,
        UUID employeeId,
        String employeeName,
        UUID serviceId,
        String serviceName,
        UUID locationId,
        String locationName,
        BigDecimal price
) {}