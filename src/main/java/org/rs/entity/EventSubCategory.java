package org.rs.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "event_subcategories")
@Data
public class EventSubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private EventCategory category;

    @Column(name = "subcategory_name", nullable = false)
    private String subCategoryName;
}
