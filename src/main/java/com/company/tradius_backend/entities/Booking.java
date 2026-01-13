package com.company.tradius_backend.entities;

import com.company.tradius_backend.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;


    // user do made the bookings
    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "vendor_id",nullable = false)
    private User user;

    // vendor fulfilling the bookings

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "vendor_id",nullable = false)
    private Vendor vendor;

    // service being booked
    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name=  "service_id",nullable = false)
    private ServiceOffering serviceOffering;

    // scheduled  service time
    @Column(nullable = false)
    private OffsetDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    // optional notes from the user
    @Column(length = 1000)
    private String notes;


    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.status = BookingStatus.REQUESTED;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = OffsetDateTime.now();
    }
}
