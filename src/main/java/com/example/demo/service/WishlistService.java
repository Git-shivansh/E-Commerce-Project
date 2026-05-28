package com.example.demo.service;

import com.example.demo.modals.User;
import com.example.demo.modals.Wishlist;

public interface WishlistService {

    Wishlist get(User user);

    Wishlist addProduct(User user, Long productId);

    Wishlist removeProduct(User user, Long productId);
}