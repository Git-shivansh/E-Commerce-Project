package com.example.demo.controller;

import com.example.demo.domain.OrderStatus;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.OrderRequest;
import com.example.demo.modals.Order;
import com.example.demo.service.AuthService;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderSvc;
    private final AuthService auth;

    @PostMapping
    public ResponseEntity<ApiResponse<List<Order>>> create(
            @RequestHeader("Authorization") String jwt,
            @RequestBody OrderRequest req
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Created",
                        orderSvc.createOrders(
                                auth.getUserFromToken(jwt.substring(7)),
                                req
                        )
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> myOrders(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        orderSvc.getUserOrders(
                                auth.getUserFromToken(jwt.substring(7))
                        )
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> get(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        orderSvc.getById(id)
                )
        );
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> cancel(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) {

        orderSvc.cancel(
                id,
                auth.getUserFromToken(jwt.substring(7))
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cancelled",
                        "success"
                )
        );
    }

    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<List<Order>>> sellerOrders(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        orderSvc.getSellerOrders(
                                auth.getSellerFromToken(jwt.substring(7))
                        )
                )
        );
    }

    @PutMapping("/seller/{id}/status")
    public ResponseEntity<ApiResponse<Order>> status(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Updated",
                        orderSvc.updateStatus(id, status)
                )
        );
    }
}