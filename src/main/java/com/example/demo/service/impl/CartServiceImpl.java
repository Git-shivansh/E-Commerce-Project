package com.example.demo.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AddToCartRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modals.Cart;
import com.example.demo.modals.CartItem;
import com.example.demo.modals.Coupon;
import com.example.demo.modals.Product;
import com.example.demo.modals.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final ProductRepository productRepo;
    private final CouponRepository couponRepo;

    @Override
    public Cart getCart(User user) {

        return cartRepo.findByUserId(user.getId())
                .orElseGet(() ->
                        cartRepo.save(
                                Cart.builder()
                                        .user(user)
                                        .build()
                        )
                );
    }

    @Override
    public Cart addItem(User user, AddToCartRequest req) {

        Cart cart = getCart(user);

        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        CartItem existing = itemRepo.findByCartAndProductAndSize(
                cart,
                product,
                req.getSize()
        ).orElse(null);

        if (existing != null) {

            existing.setQuantity(
                    existing.getQuantity() + req.getQuantity()
            );

            existing.setMrpPrice(
                    product.getMrpPrice() * existing.getQuantity()
            );

            existing.setSellingPrice(
                    product.getSellingPrice() * existing.getQuantity()
            );

            itemRepo.save(existing);

        } else {

            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .size(req.getSize())
                    .quantity(req.getQuantity())
                    .mrpPrice(product.getMrpPrice() * req.getQuantity())
                    .sellingPrice(product.getSellingPrice() * req.getQuantity())
                    .user(user)
                    .build();

            cart.getCartItems().add(
                    itemRepo.save(item)
            );
        }

        return recalculate(cart);
    }

    @Override
    public Cart updateQuantity(User user,
                               Long itemId,
                               int qty) {

        Cart cart = getCart(user);

        CartItem item = itemRepo.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found")
                );

        if (qty <= 0) {

            cart.getCartItems().remove(item);
            itemRepo.delete(item);

        } else {

            item.setQuantity(qty);

            item.setMrpPrice(
                    item.getProduct().getMrpPrice() * qty
            );

            item.setSellingPrice(
                    item.getProduct().getSellingPrice() * qty
            );

            itemRepo.save(item);
        }

        return recalculate(cart);
    }

    @Override
    public Cart removeItem(User user, Long itemId) {

        Cart cart = getCart(user);

        CartItem item = itemRepo.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Item not found")
                );

        cart.getCartItems().remove(item);

        itemRepo.delete(item);

        return recalculate(cart);
    }

    @Override
    public Cart clear(User user) {

        Cart cart = getCart(user);

        itemRepo.deleteByCartId(cart.getId());

        cart.getCartItems().clear();

        return recalculate(cart);
    }

    @Override
    public Cart applyCoupon(User user, String code) {

        Cart cart = getCart(user);

        Coupon coupon = couponRepo.findByCode(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid coupon")
                );

        if (!coupon.isActive()) {
            throw new RuntimeException("Coupon not active");
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(coupon.getValidityStartDate())
                || today.isAfter(coupon.getValidityEndDate())) {

            throw new RuntimeException("Coupon expired");
        }

        if (cart.getTotalSellingPrice()
                < coupon.getMinimumOrderValue()) {

            throw new RuntimeException(
                    "Minimum order value not met"
            );
        }

        double disc = Math.min(
                cart.getTotalSellingPrice()
                        * coupon.getDiscountPercentage()
                        / 100,

                coupon.getMaximumDiscountAmount()
        );

        cart.setCouponCode(code);
        cart.setCouponDiscount(disc);

        return cartRepo.save(cart);
    }

    @Override
    public Cart removeCoupon(User user) {

        Cart cart = getCart(user);

        cart.setCouponCode(null);
        cart.setCouponDiscount(0.0);

        return cartRepo.save(cart);
    }

    private Cart recalculate(Cart cart) {

        double mrp = 0;
        double selling = 0;
        int qty = 0;

        for (CartItem i : cart.getCartItems()) {

            mrp += i.getMrpPrice();
            selling += i.getSellingPrice();
            qty += i.getQuantity();
        }

        cart.setTotalMrpPrice(mrp);
        cart.setTotalSellingPrice(selling);

        cart.setDiscount(mrp - selling);

        cart.setTotalItems(
                cart.getCartItems().size()
        );

        cart.setTotalQuantity(qty);

        return cartRepo.save(cart);
    }
}