package com.example.demo.modals;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp;

    private String email;

    private String mobile;

    private String verificationType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt =
            LocalDateTime.now().plusMinutes(15);

    private boolean isUsed = false;
}