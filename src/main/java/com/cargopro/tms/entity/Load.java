package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
// Index for efficient filtering by shipper and status (API 2)
@Table(indexes = @Index(columnList = "shipperId, status"))
public class Load {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loadId;

    private String shipperId;
    private String loadingCity;
    private String unloadingCity;
    private String productType;
    private String truckType;
    private int noOfTrucks;
    private double weight;
    
    // Tracks current allocation for Rule 3: Multi-Truck Allocation
    private int allocatedTrucks = 0; 

    @Enumerated(EnumType.STRING)
    private LoadStatus status = LoadStatus.POSTED;

    private LocalDateTime loadingDate;
    private LocalDateTime datePosted = LocalDateTime.now();

    // Rule 4: Optimistic Locking
    @Version
    private Long version;
}
