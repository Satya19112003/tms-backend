package com.cargopro.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;

    @ManyToOne
    @JoinColumn(name = "load_id")
    private Load load;

    @OneToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    private UUID transporterId;
    private int allocatedTrucks;
    private double finalRate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.CONFIRMED;

    private LocalDateTime bookedAt = LocalDateTime.now();

    // --- MANUAL GETTERS AND SETTERS ---
    public UUID getBookingId() { return bookingId; }
    public void setBookingId(UUID bookingId) { this.bookingId = bookingId; }

    public Load getLoad() { return load; }
    public void setLoad(Load load) { this.load = load; }

    public Bid getBid() { return bid; }
    public void setBid(Bid bid) { this.bid = bid; }

    public UUID getTransporterId() { return transporterId; }
    public void setTransporterId(UUID transporterId) { this.transporterId = transporterId; }

    public int getAllocatedTrucks() { return allocatedTrucks; }
    public void setAllocatedTrucks(int allocatedTrucks) { this.allocatedTrucks = allocatedTrucks; }

    public double getFinalRate() { return finalRate; }
    public void setFinalRate(double finalRate) { this.finalRate = finalRate; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
}
