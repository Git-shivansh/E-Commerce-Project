package com.example.demo.service;

import java.util.List;

import com.example.demo.modals.Seller;
import com.example.demo.modals.SellerReport;

public interface SellerService {

    Seller getById(Long id);

    List<Seller> getAll();

    Seller update(Seller seller);

    SellerReport getReport(Seller seller);

    void updateReport(Long sellerId, double amount);
}