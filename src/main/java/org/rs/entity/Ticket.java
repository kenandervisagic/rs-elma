package org.rs.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "seat_location")
    private String seatLocation;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "purchase_start_date")
    private LocalDate purchaseStartDate;

    @Column(name = "purchase_end_date")
    private LocalDate purchaseEndDate;

    @Column(name = "cancellation_policy")
    private String cancellationPolicy;

    @Column(name = "max_purchase_per_user")
    private Integer maxPurchasePerUser;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;
}
