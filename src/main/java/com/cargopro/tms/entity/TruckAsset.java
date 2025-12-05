package com.cargopro.tms.entity;

import jakarta.persistence.*;

@Entity
public class TruckAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String truckType;
    private int count;

    // --- Manual Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTruckType() { return truckType; }
    public void setTruckType(String truckType) { this.truckType = truckType; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
