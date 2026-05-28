package com.example.demo.modals;

import com.example.demo.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_orders")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String paymentMethod;

    private String razorpayPaymentLinkId;

    private String razorpayPaymentLinkReferenceId;

    private String razorpayPaymentLinkStatus;

    private String razorpayPaymentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private Set<Order> orders = new HashSet<>();
}