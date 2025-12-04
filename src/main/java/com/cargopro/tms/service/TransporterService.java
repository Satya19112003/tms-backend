package com.cargopro.tms.service;

import com.cargopro.tms.entity.Transporter;
import com.cargopro.tms.entity.TruckAsset;
import com.cargopro.tms.repository.TransporterRepository;
import com.cargopro.tms.exception.CustomExceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransporterService {
    private final TransporterRepository transporterRepository;

    public Transporter updateTrucks(UUID transporterId, List<TruckAsset> newTrucks) {
        Transporter transporter = transporterRepository.findById(transporterId)
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found"));

        // Replace the entire list of trucks
        transporter.setAvailableTrucks(newTrucks);
        return transporterRepository.save(transporter);
    }
}
