package com.example.demo.controller;

import com.example.demo.domain.AccountStatus;
import com.example.demo.dto.ApiResponse;
import com.example.demo.modals.Seller;
import com.example.demo.modals.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepo;
    private final SellerService sellerSvc;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> users() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        userRepo.findAll()
                )
        );
    }

    @GetMapping("/sellers")
    public ResponseEntity<ApiResponse<List<Seller>>> sellers() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        sellerSvc.getAll()
                )
        );
    }

    @PutMapping("/sellers/{id}/approve")
    public ResponseEntity<ApiResponse<Seller>> approve(
            @PathVariable Long id
    ) {

        Seller s = sellerSvc.getById(id);

        s.setAccountStatus(AccountStatus.ACTIVE);
        s.setActive(true);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Approved",
                        sellerSvc.update(s)
                )
        );
    }

    @PutMapping("/sellers/{id}/suspend")
    public ResponseEntity<ApiResponse<Seller>> suspend(
            @PathVariable Long id
    ) {

        Seller s = sellerSvc.getById(id);

        s.setAccountStatus(AccountStatus.SUSPENDED);
        s.setActive(false);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Suspended",
                        sellerSvc.update(s)
                )
        );
    }
}