package com.cargopro.tms.service;

import com.cargopro.tms.entity.*;
import com.cargopro.tms.repository.*;
import com.cargopro.tms.exception.CustomExceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LoadService {
    private final LoadRepository loadRepository;
    private final BidRepository bidRepository;

    public Load createLoad(Load load) {
        load.setStatus(LoadStatus.POSTED);
        return loadRepository.save(load);
    }

    // API 2: List Loads with pagination/filtering
    public Page<Load> getLoads(String shipperId, String status, Pageable pageable) {
        if (status != null && !status.isEmpty()) {
            return loadRepository.findByShipperIdAndStatus(shipperId, LoadStatus.valueOf(status), pageable);
        }
        return loadRepository.findByShipperId(shipperId, pageable);
    }

    // API 3: Get Load details
    public Load getLoad(UUID loadId) {
        return loadRepository.findById(loadId)
            .orElseThrow(() -> new ResourceNotFoundException("Load not found"));
    }

    // API 4: Cancel Load
    @Transactional
    public void cancelLoad(UUID loadId) {
        Load load = getLoad(loadId);
        
        // Rule 2: Cannot cancel load that's already BOOKED
        if (load.getStatus() == LoadStatus.BOOKED) {
            throw new InvalidStatusTransitionException("Cannot cancel a BOOKED load.");
        }
        load.setStatus(LoadStatus.CANCELLED);
        loadRepository.save(load);
        
        // Also reject all PENDING bids for the cancelled load
        bidRepository.findByLoad_LoadId(loadId).stream()
            .filter(bid -> bid.getStatus() == BidStatus.PENDING)
            .forEach(bid -> {
                bid.setStatus(BidStatus.REJECTED);
                bidRepository.save(bid);
            });
    }

    // API 5: Best Bid Calculation (Rule 5)
    public List<Map<String, Object>> getBestBids(UUID loadId) {
        List<Bid> bids = bidRepository.findByLoad_LoadId(loadId);
        List<Map<String, Object>> scoredBids = new ArrayList<>();

        for (Bid bid : bids) {
            // Formula: score = (1 / proposedRate) * 0.7 + (rating / 5) * 0.3
            double rateScore = (1.0 / bid.getProposedRate()) * 0.7;
            double ratingScore = (bid.getTransporter().getRating() / 5.0) * 0.3;
            double score = rateScore + ratingScore;
            
            // Return bid details (excluding Load/Transporter loops) and score
            scoredBids.add(Map.of(
                "bidId", bid.getBidId(),
                "proposedRate", bid.getProposedRate(),
                "trucksOffered", bid.getTrucksOffered(),
                "transporterRating", bid.getTransporter().getRating(),
                "score", score
            ));
        }

        // Sort by score (Higher score better bid)
        scoredBids.sort((a, b) -> Double.compare((double)b.get("score"), (double)a.get("score")));
        return scoredBids;
    }
}
