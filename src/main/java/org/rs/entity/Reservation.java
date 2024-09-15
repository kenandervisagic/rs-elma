package org.rs.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "payment_amount", nullable = false)
    private Double paymentAmount;

    @Column(name = "discount_applied", nullable = false)
    private Double discountApplied;

    @Column(name = "pdf_ticket_url")
    private String pdfTicketUrl;

}
