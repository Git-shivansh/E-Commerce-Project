package com.example.demo.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coupons")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    private double discountPercentage;

    private double minimumOrderValue;

    private double maximumDiscountAmount;

    private LocalDate validityStartDate;

    private LocalDate validityEndDate;

    private boolean isActive = true;

    @ManyToMany
    @JoinTable(
            name = "coupon_users",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> usedByUsers = new HashSet<>();
}