package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Load;
import com.cargopro.tms.entity.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LoadRepository extends JpaRepository<Load, UUID> {
    // API 2: Filter by shipperId and status with pagination
    Page<Load> findByShipperIdAndStatus(String shipperId, LoadStatus status, Pageable pageable);
    
    // API 2: Filter by shipperId only with pagination
    Page<Load> findByShipperId(String shipperId, Pageable pageable);
}
