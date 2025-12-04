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
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;

    // API 1 (Booking): Accept bid & create booking
    @Transactional
    public Booking createBooking(UUID bidId) {
        Bid bid = bidRepository.findById(bidId)
            .orElseThrow(() -> new ResourceNotFoundException("Bid not found"));
        Load load = bid.getLoad();
        Transporter transporter = bid.getTransporter();

        // Check if the bid is still pending
        if (bid.getStatus() != BidStatus.PENDING) {
            throw new ConflictException("Bid status is not PENDING.");
        }

        // Rule 4/3: Final check for load availability
        int remainingNeeded = load.getNoOfTrucks() - load.getAllocatedTrucks();
        if (remainingNeeded <= 0) {
            throw new ConflictException("Load is already fully allocated.");
        }

        // Determine actual trucks to allocate
        int toAllocate = Math.min(remainingNeeded, bid.getTrucksOffered());

        // Rule 1: Deduct capacity from Transporter
        TruckAsset asset = transporter.getAvailableTrucks().stream()
            .filter(t -> t.getTruckType().equalsIgnoreCase(load.getTruckType()))
            .findFirst().orElseThrow(() -> new InsufficientCapacityException("Truck asset record missing."));
            
        if (asset.getCount() < toAllocate) {
            throw new InsufficientCapacityException("Transporter capacity has changed or is insufficient.");
        }
        asset.setCount(asset.getCount() - toAllocate);
        transporterRepository.save(transporter);

        // Update Load (Handles Rule 3 and Rule 4 Concurrency check via @Version)
        load.setAllocatedTrucks(load.getAllocatedTrucks() + toAllocate);
        
        if (load.getAllocatedTrucks() >= load.getNoOfTrucks()) {
            load.setStatus(LoadStatus.BOOKED); // Rule 2: Transition to BOOKED when fully allocated
        }
        
        loadRepository.save(load); // This is where optimistic lock check occurs

        // Mark bid as accepted
        bid.setStatus(BidStatus.ACCEPTED);
        bidRepository.save(bid);

        // Create the Booking
        Booking booking = new Booking();
        booking.setLoad(load);
        booking.setBid(bid);
        booking.setTransporterId(transporter.getTransporterId());
        booking.setAllocatedTrucks(toAllocate);
        booking.setFinalRate(bid.getProposedRate());
        
        return bookingRepository.save(booking);
    }

    // API 3 (Booking): Cancel booking
    @Transactional
    public void cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) return;

        // Rule 1: Restore trucks to available pool
        Transporter transporter = transporterRepository.findById(booking.getTransporterId())
                .orElseThrow(() -> new ResourceNotFoundException("Transporter not found for booking."));
        Load load = booking.getLoad();
        
        TruckAsset asset = transporter.getAvailableTrucks().stream()
            .filter(t -> t.getTruckType().equalsIgnoreCase(load.getTruckType()))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("Truck asset missing during cancellation."));
            
        asset.setCount(asset.getCount() + booking.getAllocatedTrucks());
        transporterRepository.save(transporter);

        // Update Load status and allocation
        load.setAllocatedTrucks(load.getAllocatedTrucks() - booking.getAllocatedTrucks());
        
        // Rule 2: If it was BOOKED and now has remaining trucks, set it back to OPEN_FOR_BIDS
        if (load.getStatus() == LoadStatus.BOOKED && load.getAllocatedTrucks() < load.getNoOfTrucks()) {
            load.setStatus(LoadStatus.OPEN_FOR_BIDS); 
        }
        loadRepository.save(load);

        // Mark booking as cancelled
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}
