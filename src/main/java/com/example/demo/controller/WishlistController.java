package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.modal.Wishlist;
import com.example.demo.service.AuthService;
import com.example.demo.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService svc;
    private final AuthService auth;

    @GetMapping
    public ResponseEntity<ApiResponse<Wishlist>> get(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        svc.get(
                                auth.getUserFromToken(jwt.substring(7))
                        )
                )
        );
    }

    @PostMapping("/{pid}")
    public ResponseEntity<ApiResponse<Wishlist>> add(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long pid
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Added",
                        svc.addProduct(
                                auth.getUserFromToken(jwt.substring(7)),
                                pid
                        )
                )
        );
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<ApiResponse<Wishlist>> remove(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long pid
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Removed",
                        svc.removeProduct(
                                auth.getUserFromToken(jwt.substring(7)),
                                pid
                        )
                )
        );
    }
}