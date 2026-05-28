package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modal.Coupon;
import com.example.demo.repository.CouponRepository;
import com.example.demo.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository repo;

    @Override
    public Coupon create(String code,
                         String name,
                         double discPct,
                         double minOrder,
                         double maxDisc,
                         LocalDate start,
                         LocalDate end) {

        if (repo.existsByCode(code)) {

            throw new RuntimeException(
                    "Coupon code already exists"
            );
        }

        Coupon coupon = Coupon.builder()
                .code(code)
                .name(name)
                .discountPercentage(discPct)
                .minimumOrderValue(minOrder)
                .maximumDiscountAmount(maxDisc)
                .validityStartDate(start)
                .validityEndDate(end)
                .isActive(true)
                .build();

        return repo.save(coupon);
    }

    @Override
    public Coupon getByCode(String code) {

        return repo.findByCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Coupon not found: " + code
                        )
                );
    }

    @Override
    public List<Coupon> getAll() {

        return repo.findAll();
    }

    @Override
    public void deactivate(Long id) {

        Coupon c = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Coupon not found"
                        )
                );

        c.setActive(false);

        repo.save(c);
    }
}