package com.example.demo.service;

import com.example.demo.modal.Seller;
import com.example.demo.modal.SellerReport;

import java.util.List;

public interface SellerService {

    Seller getById(Long id);

    List<Seller> getAll();

    Seller update(Seller seller);

    SellerReport getReport(Seller seller);

    void updateReport(Long sellerId, double amount);
}