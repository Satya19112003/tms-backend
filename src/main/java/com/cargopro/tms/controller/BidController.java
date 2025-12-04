package com.cargopro.tms.controller;

import com.cargopro.tms.entity.Bid;
import com.cargopro.tms.service.BidService;
import com.cargopro.tms.repository.BidRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.*;

@RestController
@RequestMapping("/bid")
@RequiredArgsConstructor
@Tag(name = "Bid APIs", description = "Operations for submitting and managing bids on loads.")
public class BidController {
    private final BidService bidService;
    private final BidRepository bidRepository;

    // 1. POST /bid
    @Operation(summary = "Submit Bid", description = "Submits a bid. Validates transporter capacity and load status (Rule 1, Rule 2).")
    @PostMapping
    public Bid submitBid(@RequestParam UUID loadId, 
                         @RequestParam UUID transporterId, 
                         @RequestParam double rate, 
                         @RequestParam int trucks) {
        return bidService.submitBid(loadId, transporterId, rate, trucks);
    }

    // 2. GET /bid
    @Operation(summary = "Filter Bids", description = "Lists bids, primarily filtered by loadId and optionally by transporterId.")
    @GetMapping
    public List<Bid> getBids(@RequestParam UUID loadId, @RequestParam(required=false) UUID transporterId) {
        if(transporterId != null) return bidRepository.findByLoad_LoadIdAndTransporter_TransporterId(loadId, transporterId);
        return bidRepository.findByLoad_LoadId(loadId);
    }

    // 3. GET /bid/{bidId}
    @Operation(summary = "Get Bid Details", description = "Retrieves details for a specific bid.")
    @GetMapping("/{id}")
    public Bid getBid(@PathVariable UUID id) {
        return bidRepository.findById(id).orElseThrow();
    }
    
    // 4. PATCH /bid/{bidId}/reject
    @Operation(summary = "Reject Bid", description = "Sets the bid status to REJECTED. Cannot reject an ACCEPTED bid.")
    @PatchMapping("/{id}/reject")
    public Bid rejectBid(@PathVariable UUID id) {
        return bidService.rejectBid(id);
    }
}
