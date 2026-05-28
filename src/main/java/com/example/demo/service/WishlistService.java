package com.example.demo.service;

import com.example.demo.modal.User;
import com.example.demo.modal.Wishlist;

public interface WishlistService {

    Wishlist get(User user);

    Wishlist addProduct(User user, Long productId);

    Wishlist removeProduct(User user, Long productId);
}