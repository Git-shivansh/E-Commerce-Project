package com.example.demo.service;

import com.example.demo.modal.Order;
import com.example.demo.modal.PaymentOrder;
import com.example.demo.modal.User;

import java.util.List;

public interface PaymentService {

    PaymentOrder create(User user,
                        List<Order> orders,
                        String method);

    PaymentOrder verify(String paymentOrderId,
                        String razorpayPaymentId,
                        String signature);

    List<PaymentOrder> getByUser(User user);
}