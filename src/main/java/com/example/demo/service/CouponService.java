package com.example.demo.service;

import com.example.demo.modal.Coupon;

import java.time.LocalDate;
import java.util.List;

public interface CouponService {

    Coupon create(String code,
                  String name,
                  double discPct,
                  double minOrder,
                  double maxDisc,
                  LocalDate start,
                  LocalDate end);

    Coupon getByCode(String code);

    List<Coupon> getAll();

    void deactivate(Long id);
}