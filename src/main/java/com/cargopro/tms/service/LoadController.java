package com.cargopro.tms.controller;

import com.cargopro.tms.entity.Load;
import com.cargopro.tms.service.LoadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.*;

@RestController
@RequestMapping("/load")
@RequiredArgsConstructor
@Tag(name = "Load APIs", description = "Operations for posting, listing, and managing loads.")
public class LoadController {
    private final LoadService loadService;

    // 1. POST /load
    @Operation(summary = "Create Load", description = "Creates a new load. Status is POSTED.")
    @PostMapping
    public Load createLoad(@RequestBody Load load) {
        return loadService.createLoad(load);
    }

    // 2. GET /load
    @Operation(summary = "List Loads", description = "Lists loads with pagination, filtered by shipperId and optional status.")
    @GetMapping
    public Page<Load> getLoads(@RequestParam String shipperId, 
                               @RequestParam(required = false) String status,
                               @RequestParam(defaultValue = "0") int page, 
                               @RequestParam(defaultValue = "10") int size) {
        return loadService.getLoads(shipperId, status, PageRequest.of(page, size));
    }

    // 3. GET /load/{loadId}
    @Operation(summary = "Get Load", description = "Retrieves details of a specific load.")
    @GetMapping("/{loadId}")
    public Load getLoad(@PathVariable UUID loadId) {
        return loadService.getLoad(loadId);
    }

    // 4. PATCH /load/{loadId}/cancel
    @Operation(summary = "Cancel Load", description = "Cancels a load (if not already BOOKED). Status changes to CANCELLED.")
    @PatchMapping("/{loadId}/cancel")
    public void cancelLoad(@PathVariable UUID loadId) {
        loadService.cancelLoad(loadId);
    }

    // 5. GET /load/{loadId}/best-bids
    @Operation(summary = "Get Best Bids", description = "Returns active bids sorted by the best-bid score (Rate 70%, Rating 30%).")
    @GetMapping("/{loadId}/best-bids")
    public List<Map<String, Object>> getBestBids(@PathVariable UUID loadId) {
        return loadService.getBestBids(loadId);
    }
}
