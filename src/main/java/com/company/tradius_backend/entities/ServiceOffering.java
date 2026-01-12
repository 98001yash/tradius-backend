package com.company.tradius_backend.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "service_offering")
public class ServiceOffering {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;


    // vendor who provides the service
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    // service details
    @Column(nullable = false)
    private String name;

    @Column(length =1000)
    private String description;

    @Column(nullable = false)
    private Double price;

    private Integer durationMinutes;

    @Column(nullable = false)
    private boolean active;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        this.active = true;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = OffsetDateTime.now();
    }
}
