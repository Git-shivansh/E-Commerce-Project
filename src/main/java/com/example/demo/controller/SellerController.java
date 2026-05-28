package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.modals.Seller;
import com.example.demo.modals.SellerReport;
import com.example.demo.service.AuthService;
import com.example.demo.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService svc;
    private final AuthService auth;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Seller>> profile(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        auth.getSellerFromToken(jwt.substring(7))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Seller>> update(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Seller u
    ) {

        Seller s = auth.getSellerFromToken(jwt.substring(7));

        s.setSellerName(u.getSellerName());
        s.setBusinessName(u.getBusinessName());
        s.setBusinessDescription(u.getBusinessDescription());
        s.setMobile(u.getMobile());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Updated",
                        svc.update(s)
                )
        );
    }

    @GetMapping("/report")
    public ResponseEntity<ApiResponse<SellerReport>> report(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        svc.getReport(
                                auth.getSellerFromToken(jwt.substring(7))
                        )
                )
        );
    }
}