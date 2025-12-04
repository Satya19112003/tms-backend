package com.cargopro.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(indexes = @Index(columnList = "load_id, transporter_id"))
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bidId;

    @ManyToOne
    @JoinColumn(name = "load_id")
    private Load load;

    @ManyToOne
    @JoinColumn(name = "transporter_id")
    private Transporter transporter;

    private double proposedRate;
    private int trucksOffered;

    @Enumerated(EnumType.STRING)
    private BidStatus status = BidStatus.PENDING;

    private LocalDateTime submittedAt = LocalDateTime.now();

    // --- MANUAL GETTERS AND SETTERS ---
    public UUID getBidId() { return bidId; }
    public void setBidId(UUID bidId) { this.bidId = bidId; }

    public Load getLoad() { return load; }
    public void setLoad(Load load) { this.load = load; }

    public Transporter getTransporter() { return transporter; }
    public void setTransporter(Transporter transporter) { this.transporter = transporter; }

    public double getProposedRate() { return proposedRate; }
    public void setProposedRate(double proposedRate) { this.proposedRate = proposedRate; }

    public int getTrucksOffered() { return trucksOffered; }
    public void setTrucksOffered(int trucksOffered) { this.trucksOffered = trucksOffered; }

    public BidStatus getStatus() { return status; }
    public void setStatus(BidStatus status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
