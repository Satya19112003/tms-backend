package com.cargopro.tms.controller;

import com.cargopro.tms.entity.Booking;
import com.cargopro.tms.service.BookingService;
import com.cargopro.tms.repository.BookingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
@Tag(name = "Booking APIs", description = "Operations for accepting bids, creating, and cancelling bookings.")
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    // 1. POST /booking
    @Operation(summary = "Accept Bid & Create Booking", description = "Accepts a bid, creates a booking, deducts trucks (Rule 1), handles multi-truck allocation (Rule 3), and prevents concurrency (Rule 4).")
    @PostMapping
    public Booking createBooking(@RequestParam UUID bidId) {
        return bookingService.createBooking(bidId);
    }

    // 2. GET /booking/{bookingId}
    @Operation(summary = "Get Booking Details", description = "Retrieves details for a specific booking.")
    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable UUID id) {
        return bookingRepository.findById(id).orElseThrow();
    }

    // 3. PATCH /booking/{bookingId}/cancel
    @Operation(summary = "Cancel Booking", description = "Cancels a booking, restores allocated trucks to the transporter's pool (Rule 1), and potentially reopens the load (Rule 2).")
    @PatchMapping("/{id}/cancel")
    public void cancelBooking(@PathVariable UUID id) {
        bookingService.cancelBooking(id);
    }
}
