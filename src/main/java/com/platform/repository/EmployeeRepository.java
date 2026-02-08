package com.platform.repository;

import com.platform.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByBusinessId(UUID businessId);
    List<Employee> findByBusinessIdAndActive(UUID businessId, Boolean active);
}
