package com.platform.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record EmployeeLocationServicePriceRequestDTO(
        UUID employeeId,
        UUID serviceId,
        UUID locationId,
        BigDecimal price
) {}