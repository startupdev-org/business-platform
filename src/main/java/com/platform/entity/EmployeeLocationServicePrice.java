package com.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "employee_location_service_price",
       uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "service_id", "location_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeLocationServicePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ProvidedService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;  // can point to "default location" for no-location businesses

    @Column(nullable = false)
    private BigDecimal price;

//    private LocalDateTime validFrom; // optional
//    private LocalDateTime validTo;   // optional
}
