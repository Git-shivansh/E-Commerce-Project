package com.example.demo.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> cartItems = new ArrayList<>();

    private double totalMrpPrice = 0.0;

    private double totalSellingPrice = 0.0;

    private double discount = 0.0;

    private int totalItems = 0;

    private int totalQuantity = 0;

    private String couponCode;

    private double couponDiscount = 0.0;
}