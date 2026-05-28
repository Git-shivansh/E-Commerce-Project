package com.example.demo.modals;

import com.example.demo.domain.OrderStatus;
import com.example.demo.domain.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL
    )
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Address shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private double totalMrpPrice;

    private double totalSellingPrice;

    private double discount;

    private int totalItems;

    private String couponCode;

    private double couponDiscount = 0.0;

    private String paymentId;

    private String razorpayOrderId;

    @Column(updatable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    private LocalDateTime deliveryDate;
}