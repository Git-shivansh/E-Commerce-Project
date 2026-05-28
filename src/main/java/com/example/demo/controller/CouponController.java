package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.modals.Coupon;
import com.example.demo.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService svc;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Coupon>>> all() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        svc.getAll()
                )
        );
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<ApiResponse<Coupon>> validate(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Valid",
                        svc.getByCode(code)
                )
        );
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Coupon>> create(
            @RequestBody Map<String, Object> b
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Created",
                        svc.create(
                                (String) b.get("code"),
                                (String) b.get("name"),
                                Double.parseDouble(b.get("discountPercentage").toString()),
                                Double.parseDouble(b.get("minimumOrderValue").toString()),
                                Double.parseDouble(b.get("maximumDiscountAmount").toString()),
                                LocalDate.parse(b.get("validityStartDate").toString()),
                                LocalDate.parse(b.get("validityEndDate").toString())
                        )
                )
        );
    }

    @PutMapping("/admin/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deactivate(
            @PathVariable Long id
    ) {

        svc.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Deactivated",
                        "success"
                )
        );
    }
}