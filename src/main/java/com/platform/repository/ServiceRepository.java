package com.platform.repository;

import com.platform.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    List<Service> findByBusinessId(UUID businessId);
    List<Service> findByBusinessIdAndActive(UUID businessId, Boolean active);
}
