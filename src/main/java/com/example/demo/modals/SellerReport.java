package com.example.demo.modals;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "seller_reports")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SellerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "seller_id", unique = true)
    private Seller seller;

    private double totalEarnings = 0.0;

    private double totalSales = 0.0;

    private long totalOrders = 0;

    private long totalRefunds = 0;

    private double totalTax = 0.0;

    private double netEarnings = 0.0;

    private double platformFee = 0.0;

    private LocalDate startTime;

    private LocalDate endTime;
}