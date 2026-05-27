package com.example.demo.controller;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.dto.ApiResponse;
import com.example.demo.modal.Cart;
import com.example.demo.service.AuthService;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartSvc;
    private final AuthService auth;

    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> get(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        cartSvc.getCart(
                                auth.getUserFromToken(jwt.substring(7))
                        )
                )
        );
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> add(
            @RequestHeader("Authorization") String jwt,
            @RequestBody AddToCartRequest req
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Added",
                        cartSvc.addItem(
                                auth.getUserFromToken(jwt.substring(7)),
                                req
                        )
                )
        );
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Cart>> update(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id,
            @RequestParam int quantity
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Updated",
                        cartSvc.updateQuantity(
                                auth.getUserFromToken(jwt.substring(7)),
                                id,
                                quantity
                        )
                )
        );
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Cart>> remove(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Removed",
                        cartSvc.removeItem(
                                auth.getUserFromToken(jwt.substring(7)),
                                id
                        )
                )
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Cart>> clear(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cleared",
                        cartSvc.clear(
                                auth.getUserFromToken(jwt.substring(7))
                        )
                )
        );
    }

    @PostMapping("/coupon/apply")
    public ResponseEntity<ApiResponse<Cart>> applyCoupon(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String code
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applied",
                        cartSvc.applyCoupon(
                                auth.getUserFromToken(jwt.substring(7)),
                                code
                        )
                )
        );
    }

    @DeleteMapping("/coupon/remove")
    public ResponseEntity<ApiResponse<Cart>> removeCoupon(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Removed",
                        cartSvc.removeCoupon(
                                auth.getUserFromToken(jwt.substring(7))
                        )
                )
        );
    }
}