package com.example.demo.service.impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.domain.PaymentStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modals.Order;
import com.example.demo.modals.PaymentOrder;
import com.example.demo.modals.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentOrderRepository;
import com.example.demo.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentRepo;
    private final OrderRepository orderRepo;

    @Value("${razorpay.api.key:key}")
    private String rzpKey;

    @Value("${razorpay.api.secret:secret}")
    private String rzpSecret;

    @Override
    public PaymentOrder create(User user,
                               List<Order> orders,
                               String method) {

        double total = orders.stream()
                .mapToDouble(Order::getTotalSellingPrice)
                .sum();

        PaymentOrder po = PaymentOrder.builder()
                .amount(total)
                .status(PaymentStatus.PENDING)
                .paymentMethod(method)
                .user(user)
                .orders(new HashSet<>(orders))
                .build();

        // Integrate Razorpay SDK here in production

        po.setRazorpayPaymentLinkId(
                "rzp_" + System.currentTimeMillis()
        );

        return paymentRepo.save(po);
    }

    @Override
    public PaymentOrder verify(String paymentOrderId,
                               String razorpayPaymentId,
                               String signature) {

        // Verify Razorpay signature in production

        PaymentOrder po = paymentRepo
                .findByRazorpayPaymentLinkId(paymentOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment order not found"
                        )
                );

        po.setStatus(
                PaymentStatus.COMPLETED
        );

        po.setRazorpayPaymentId(
                razorpayPaymentId
        );

        po.getOrders().forEach(order -> {

            order.setPaymentStatus(
                    PaymentStatus.COMPLETED
            );

            order.setPaymentId(
                    razorpayPaymentId
            );

            orderRepo.save(order);
        });

        return paymentRepo.save(po);
    }

    @Override
    public List<PaymentOrder> getByUser(User user) {

        return paymentRepo.findByUserId(
                user.getId()
        );
    }
}