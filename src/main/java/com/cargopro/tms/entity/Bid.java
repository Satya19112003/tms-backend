package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(indexes = @Index(columnList = "load_id, transporter_id"))
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bidId;

    // Foreign Key to Load
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id", nullable = false)
    private Load load;

    // Foreign Key to Transporter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transporter_id", nullable = false)
    private Transporter transporter;

    private double proposedRate;
    private int trucksOffered;

    @Enumerated(EnumType.STRING)
    private BidStatus status = BidStatus.PENDING;

    private LocalDateTime submittedAt = LocalDateTime.now();
}
