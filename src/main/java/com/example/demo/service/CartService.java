package com.example.demo.service;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.modal.Cart;
import com.example.demo.modal.User;

public interface CartService {

    Cart getCart(User user);

    Cart addItem(User user, AddToCartRequest req);

    Cart updateQuantity(User user, Long itemId, int qty);

    Cart removeItem(User user, Long itemId);

    Cart clear(User user);

    Cart applyCoupon(User user, String code);

    Cart removeCoupon(User user);
}