package com.cargopro.tms.service;

import com.cargopro.tms.entity.*;
import com.cargopro.tms.repository.*;
import com.cargopro.tms.exception.CustomExceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;

    // API 1 (Bid): Submit Bid
    @Transactional
    public Bid submitBid(UUID loadId, UUID transporterId, double rate, int trucksOffered) {
        Load load = loadRepository.findById(loadId)
            .orElseThrow(() -> new ResourceNotFoundException("Load not found"));
        Transporter transporter = transporterRepository.findById(transporterId)
            .orElseThrow(() -> new ResourceNotFoundException("Transporter not found"));

        // Rule 2: Cannot bid on CANCELLED or BOOKED loads
        if (load.getStatus() == LoadStatus.BOOKED || load.getStatus() == LoadStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Load is not open for bids or is finalized.");
        }

        // Rule 1: Capacity Validation
        TruckAsset asset = transporter.getAvailableTrucks().stream()
            .filter(t -> t.getTruckType().equalsIgnoreCase(load.getTruckType()))
            .findFirst()
            .orElseThrow(() -> new InsufficientCapacityException("Transporter does not list this truck type."));

        if (asset.getCount() < trucksOffered) {
            throw new InsufficientCapacityException("Transporter does not have enough trucks available to bid.");
        }

        Bid bid = new Bid();
        bid.setLoad(load);
        bid.setTransporter(transporter);
        bid.setProposedRate(rate);
        bid.setTrucksOffered(trucksOffered);
        
        // Rule 2: Load Status Transition to OPEN_FOR_BIDS (when first bid received)
        if (load.getStatus() == LoadStatus.POSTED) {
            load.setStatus(LoadStatus.OPEN_FOR_BIDS);
            loadRepository.save(load);
        }
        
        return bidRepository.save(bid);
    }

    // API 4 (Bid): Reject Bid
    @Transactional
    public Bid rejectBid(UUID bidId) {
        Bid bid = bidRepository.findById(bidId)
            .orElseThrow(() -> new ResourceNotFoundException("Bid not found"));

        if (bid.getStatus() == BidStatus.ACCEPTED) {
            throw new InvalidStatusTransitionException("Cannot reject an ACCEPTED bid.");
        }

        bid.setStatus(BidStatus.REJECTED);
        return bidRepository.save(bid);
    }
}
