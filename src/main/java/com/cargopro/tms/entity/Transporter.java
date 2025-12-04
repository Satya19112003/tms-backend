package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Transporter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transporterId;

    private String companyName;
    private double rating; // 1.0 to 5.0 (used in Rule 5: Best Bid)

    // CascadeType.ALL ensures trucks are saved/deleted with the Transporter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TruckAsset> availableTrucks = new ArrayList<>();
}
