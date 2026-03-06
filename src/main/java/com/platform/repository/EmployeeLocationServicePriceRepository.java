package com.platform.repository;

import com.platform.entity.EmployeeLocationServicePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeLocationServicePriceRepository extends JpaRepository<EmployeeLocationServicePrice, UUID> {

    List<EmployeeLocationServicePrice> findByEmployeeId(UUID employeeId);

    List<EmployeeLocationServicePrice> findByServiceId(UUID serviceId);

    List<EmployeeLocationServicePrice> findByLocationId(UUID locationId);

    Optional<EmployeeLocationServicePrice> findByEmployeeIdAndServiceIdAndLocationId(
            UUID employeeId, UUID serviceId, UUID locationId);

    boolean existsByEmployeeIdAndServiceIdAndLocationId(
            UUID employeeId, UUID serviceId, UUID locationId);

    @Query("""
            SELECT p FROM EmployeeLocationServicePrice p
            WHERE p.employee.id = :employeeId
              AND p.location.id = :locationId
            """)
    List<EmployeeLocationServicePrice> findByEmployeeIdAndLocationId(
            @Param("employeeId") UUID employeeId,
            @Param("locationId") UUID locationId);
}