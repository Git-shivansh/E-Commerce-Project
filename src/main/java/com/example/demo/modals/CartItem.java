package com.example.demo.modals;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private Double price;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;
}