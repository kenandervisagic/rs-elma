package org.rs.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private EventCategory category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private EventSubCategory subCategory;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "description")
    private String description;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time", nullable = false)
    private LocalTime eventTime;


    @Column(name = "max_tickets", nullable = false)
    private Integer maxTickets;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location locationEntity;

    @OneToMany(mappedBy = "event")
    private Set<Ticket> tickets;
}
