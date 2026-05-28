package com.example.demo.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private String imageUrl;

    private int level;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parentCategory;

    @OneToMany(
            mappedBy = "parentCategory",
            cascade = CascadeType.ALL
    )
    private List<Category> subCategories = new ArrayList<>();
}