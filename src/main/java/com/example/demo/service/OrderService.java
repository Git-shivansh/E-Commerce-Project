package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.OrderStatus;
import com.example.demo.dto.OrderRequest;
import com.example.demo.modals.Order;
import com.example.demo.modals.Seller;
import com.example.demo.modals.User;

public interface OrderService {

    List<Order> createOrders(User user, OrderRequest req);

    Order getById(Long id);

    List<Order> getUserOrders(User user);

    List<Order> getSellerOrders(Seller seller);

    Order updateStatus(Long id, OrderStatus status);

    void cancel(Long id, User user);
}