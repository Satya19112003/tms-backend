package com.cargopro.tms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
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
    private int allocatedTrucks = 0;

    @Enumerated(EnumType.STRING)
    private LoadStatus status = LoadStatus.POSTED;

    private LocalDateTime loadingDate;
    private LocalDateTime datePosted = LocalDateTime.now();

    @Version
    private Long version;

    // --- MANUAL GETTERS AND SETTERS ---
    public UUID getLoadId() { return loadId; }
    public void setLoadId(UUID loadId) { this.loadId = loadId; }

    public String getShipperId() { return shipperId; }
    public void setShipperId(String shipperId) { this.shipperId = shipperId; }

    public String getLoadingCity() { return loadingCity; }
    public void setLoadingCity(String loadingCity) { this.loadingCity = loadingCity; }

    public String getUnloadingCity() { return unloadingCity; }
    public void setUnloadingCity(String unloadingCity) { this.unloadingCity = unloadingCity; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getTruckType() { return truckType; }
    public void setTruckType(String truckType) { this.truckType = truckType; }

    public int getNoOfTrucks() { return noOfTrucks; }
    public void setNoOfTrucks(int noOfTrucks) { this.noOfTrucks = noOfTrucks; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getAllocatedTrucks() { return allocatedTrucks; }
    public void setAllocatedTrucks(int allocatedTrucks) { this.allocatedTrucks = allocatedTrucks; }

    public LoadStatus getStatus() { return status; }
    public void setStatus(LoadStatus status) { this.status = status; }

    public LocalDateTime getLoadingDate() { return loadingDate; }
    public void setLoadingDate(LocalDateTime loadingDate) { this.loadingDate = loadingDate; }

    public LocalDateTime getDatePosted() { return datePosted; }
    public void setDatePosted(LocalDateTime datePosted) { this.datePosted = datePosted; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
