package com.example.MIS.Invoicing.System.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "chain")
public class Chain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chainId;

    @Column(nullable = false, length = 255)
    private String companyName;

    @Column(nullable = false, unique = true, length = 15)
    private String gstnNo;

    // Many chains belong to one group
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

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