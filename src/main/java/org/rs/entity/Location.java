package org.rs.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "locations")
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer id;

    @Column(name = "location_name", nullable = false, unique = true)
    private String locationName;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "image_url")
    private String type;

    @OneToMany(mappedBy = "locationEntity")
    private Set<Event> events;

    @OneToMany(mappedBy = "location")
    private Set<Sector> sectors;
}
