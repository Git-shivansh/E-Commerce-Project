package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modals.Seller;
import com.example.demo.modals.SellerReport;
import com.example.demo.repository.SellerReportRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.service.SellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepo;
    private final SellerReportRepository reportRepo;

    @Override
    public Seller getById(Long id) {

        return sellerRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seller not found: " + id
                        )
                );
    }

    @Override
    public List<Seller> getAll() {

        return sellerRepo.findAll();
    }

    @Override
    public Seller update(Seller seller) {

        return sellerRepo.save(seller);
    }

    @Override
    public SellerReport getReport(Seller seller) {

        return reportRepo.findBySellerId(seller.getId())
                .orElseGet(() ->
                        reportRepo.save(
                                SellerReport.builder()
                                        .seller(seller)
                                        .build()
                        )
                );
    }

    @Override
    public void updateReport(Long sellerId,
                             double amount) {

        Seller seller = getById(sellerId);

        SellerReport report = getReport(seller);

        report.setTotalOrders(
                report.getTotalOrders() + 1
        );

        report.setTotalSales(
                report.getTotalSales() + amount
        );

        double fee = amount * 0.02;

        report.setPlatformFee(
                report.getPlatformFee() + fee
        );

        report.setNetEarnings(
                report.getTotalSales()
                        - report.getPlatformFee()
                        - report.getTotalTax()
        );

        reportRepo.save(report);
    }
}