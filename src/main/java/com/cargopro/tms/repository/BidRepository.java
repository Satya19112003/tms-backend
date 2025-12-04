package com.cargopro.tms.repository;

import com.cargopro.tms.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    // API 3: Get all bids for a load
    List<Bid> findByLoad_LoadId(UUID loadId);
    
    // API 2 (Bid): Filter by load and transporter
    List<Bid> findByLoad_LoadIdAndTransporter_TransporterId(UUID loadId, UUID transporterId);
}
