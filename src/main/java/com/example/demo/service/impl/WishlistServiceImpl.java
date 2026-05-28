package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modals.Product;
import com.example.demo.modals.User;
import com.example.demo.modals.Wishlist;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.service.WishlistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishRepo;
    private final ProductRepository productRepo;

    @Override
    public Wishlist get(User user) {

        return wishRepo.findByUserId(user.getId())
                .orElseGet(() ->
                        wishRepo.save(
                                Wishlist.builder()
                                        .user(user)
                                        .build()
                        )
                );
    }

    @Override
    public Wishlist addProduct(User user,
                               Long productId) {

        Wishlist w = get(user);

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        w.getProducts().add(product);

        return wishRepo.save(w);
    }

    @Override
    public Wishlist removeProduct(User user,
                                  Long productId) {

        Wishlist w = get(user);

        w.getProducts().removeIf(
                p -> p.getId().equals(productId)
        );

        return wishRepo.save(w);
    }
}