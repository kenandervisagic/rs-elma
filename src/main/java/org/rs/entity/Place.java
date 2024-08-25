package org.rs.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "town_id")
    private Integer id;

    @Column(name = "town", nullable = false, unique = true)
    private String locationName;
}
