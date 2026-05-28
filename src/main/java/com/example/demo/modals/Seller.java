package com.example.demo.modals;

import com.example.demo.domain.AccountStatus;
import com.example.demo.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sellers")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sellerName;

    private String email;

    @JsonIgnore
    private String password;

    private String mobile;

    private String businessName;

    private String businessDescription;

    private String gstNumber;

    private String bankAccountNumber;

    private String ifscCode;

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus =
            AccountStatus.PENDING_VERIFICATION;

    @Enumerated(EnumType.STRING)
    private USER_ROLE role = USER_ROLE.ROLE_SELLER;

    private boolean isActive = false;

    private Double rating = 0.0;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}