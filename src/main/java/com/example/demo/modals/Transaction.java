package com.example.demo.modals;

import com.example.demo.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private double amount;

    private String paymentMethod;

    private String transactionId;

    @Column(updatable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();
}