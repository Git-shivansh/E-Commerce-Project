package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.modals.Order;
import com.example.demo.modals.PaymentOrder;
import com.example.demo.modals.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentSvc;
    private final OrderService orderSvc;
    private final AuthService auth;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PaymentOrder>> create(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Map<String, Object> body
    ) {

        User user = auth.getUserFromToken(jwt.substring(7));

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) body.get("orderIds");

        List<Order> orders = ids.stream()
                .map(orderSvc::getById)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Created",
                        paymentSvc.create(
                                user,
                                orders,
                                (String) body.get("paymentMethod")
                        )
                )
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<PaymentOrder>> verify(
            @RequestBody Map<String, String> body
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Verified",
                        paymentSvc.verify(
                                body.get("paymentOrderId"),
                                body.get("razorpayPaymentId"),
                                body.get("razorpaySignature")
                        )
                )
        );
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<PaymentOrder>>> myPayments(
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        paymentSvc.getByUser(
                                auth.getUserFromToken(jwt.substring(7))
                        )
                )
        );
    }
}