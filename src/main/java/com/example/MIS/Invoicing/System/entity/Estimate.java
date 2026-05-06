package com.example.MIS.Invoicing.System.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "estimates")
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer estimatedId;

    @Column(name = "chain_id", nullable = false)
    private Integer chainId;

    @Column(name = "group_name", length = 50)
    private String groupName;

    @Column(name = "brand_name", length = 50)
    private String brandName;

    @Column(name = "zone_name", length = 50)
    private String zoneName;

    @Column(name = "service", length = 100)
    private String service;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "cost_per_unit")
    private Float costPerUnit;

    @Column(name = "total_cost")
    private Float totalCost;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "delivery_details", length = 100)
    private String deliveryDetails;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}