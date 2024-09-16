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

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time", nullable = false)
    private LocalTime eventTime;

    @Column(name = "max_tickets", nullable = false)
    private Integer maxTickets;

    @Column(name ="ticket_price", nullable = false)
    private double price;

    @Column (name="approved")
    private boolean approved;

    @Column (name="active")
    private boolean active;

    @Column (name="cancel_policy", nullable = false)
    private boolean cancelPolicy; //True -> can cancel

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location locationEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_sectors",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "sector_id")
    )
    private Set<Sector> sectors;  // Set of sectors where the event will take place



    @Override
    public String toString() {
        return String.format("%s - %s -> %s at %s on %s",
                eventName,
                category.getCategoryName(),
                subCategory.getSubCategoryName(),
                locationEntity.getLocationName(),
                eventDate.toString()
        );
    }
}
