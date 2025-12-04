package com.cargopro.tms.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Transporter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transporterId;

    private String companyName;
    private double rating;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TruckAsset> availableTrucks = new ArrayList<>();

    // --- MANUAL GETTERS AND SETTERS ---
    public UUID getTransporterId() { return transporterId; }
    public void setTransporterId(UUID transporterId) { this.transporterId = transporterId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public List<TruckAsset> getAvailableTrucks() { return availableTrucks; }
    public void setAvailableTrucks(List<TruckAsset> availableTrucks) { this.availableTrucks = availableTrucks; }
}
