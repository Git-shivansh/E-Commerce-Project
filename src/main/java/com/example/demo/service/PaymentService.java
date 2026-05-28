package com.example.demo.service;

import java.util.List;

import com.example.demo.modals.Order;
import com.example.demo.modals.PaymentOrder;
import com.example.demo.modals.User;

public interface PaymentService {

    PaymentOrder create(User user,
                        List<Order> orders,
                        String method);

    PaymentOrder verify(String paymentOrderId,
                        String razorpayPaymentId,
                        String signature);

    List<PaymentOrder> getByUser(User user);
}