package com.cargopro.tms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TruckAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String truckType;
    private int count; // Number of available trucks of this type
}
