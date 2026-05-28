package com.example.demo.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String streetAddress;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private String mobile;

    private boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}