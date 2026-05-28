package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.modals.Cart;
import com.example.demo.modals.CartItem;
import com.example.demo.modals.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProductAndSize(
            Cart cart,
            Product product,
            String size
    );

    void deleteByCartId(Long cartId);
}