package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modals.SellerReport;

@Repository
public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {

    Optional<SellerReport> findBySellerId(Long sellerId);
}