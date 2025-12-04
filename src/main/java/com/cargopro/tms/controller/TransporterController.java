package com.cargopro.tms.controller;

import com.cargopro.tms.entity.Transporter;
import com.cargopro.tms.entity.TruckAsset;
import com.cargopro.tms.repository.TransporterRepository;
import com.cargopro.tms.service.TransporterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transporter")
@RequiredArgsConstructor
@Tag(name = "Transporter APIs", description = "Operations for registering and managing transporter capacity.")
public class TransporterController {
    private final TransporterRepository transporterRepository;
    private final TransporterService transporterService;

    // 1. POST /transporter
    @Operation(summary = "Register Transporter", description = "Registers a new transporter with initial truck capacity.")
    @PostMapping
    public Transporter register(@RequestBody Transporter transporter) {
        return transporterRepository.save(transporter);
    }

    // 2. GET /transporter/{transporterId}
    @Operation(summary = "Get Transporter", description = "Retrieves transporter details, including current available trucks.")
    @GetMapping("/{id}")
    public Transporter get(@PathVariable UUID id) {
        return transporterRepository.findById(id).orElseThrow();
    }

    // 3. PUT /transporter/{transporterId}/trucks
    @Operation(summary = "Update Trucks", description = "Updates the entire list of available trucks for a transporter (replaces existing list).")
    @PutMapping("/{id}/trucks")
    public Transporter updateTrucks(@PathVariable UUID id, @RequestBody List<TruckAsset> trucks) {
        return transporterService.updateTrucks(id, trucks);
    }
}
