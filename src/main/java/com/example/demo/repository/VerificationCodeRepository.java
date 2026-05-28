package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modals.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findByEmailAndOtpAndIsUsedFalse(
            String email,
            String otp
    );

    Optional<VerificationCode> findTopByEmailOrderByCreatedAtDesc(
            String email
    );
}