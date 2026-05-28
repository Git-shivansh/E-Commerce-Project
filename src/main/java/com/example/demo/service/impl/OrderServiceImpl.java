package com.example.demo.service.impl;

import com.example.demo.domain.OrderStatus;
import com.example.demo.dto.OrderRequest;
import com.example.demo.exception.OrderException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modal.Address;
import com.example.demo.modal.Cart;
import com.example.demo.modal.CartItem;
import com.example.demo.modal.Order;
import com.example.demo.modal.OrderItem;
import com.example.demo.modal.Seller;
import com.example.demo.modal.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final AddressRepository addressRepo;

    @Override
    public List<Order> createOrders(User user,
                                    OrderRequest req) {

        Address addr = addressRepo.findById(req.getAddressId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found"
                        )
                );

        Cart cart = cartRepo.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"
                        )
                );

        if (cart.getCartItems().isEmpty()) {
            throw new OrderException("Cart is empty");
        }

        Map<Seller, List<CartItem>> bySeller = new HashMap<>();

        for (CartItem ci : cart.getCartItems()) {

            bySeller.computeIfAbsent(
                    ci.getProduct().getSeller(),
                    k -> new ArrayList<>()
            ).add(ci);
        }

        List<Order> orders = new ArrayList<>();

        for (Map.Entry<Seller, List<CartItem>> entry : bySeller.entrySet()) {

            Order order = new Order();

            order.setOrderId(
                    "ORD-" + System.currentTimeMillis()
            );

            order.setUser(user);
            order.setSeller(entry.getKey());

            order.setShippingAddress(addr);

            order.setDeliveryDate(
                    LocalDateTime.now().plusDays(7)
            );

            order.setCouponCode(
                    cart.getCouponCode()
            );

            double mrp = 0;
            double selling = 0;

            List<OrderItem> items = new ArrayList<>();

            for (CartItem ci : entry.getValue()) {

                OrderItem item = OrderItem.builder()
                        .order(order)
                        .product(ci.getProduct())
                        .size(ci.getSize())
                        .quantity(ci.getQuantity())
                        .mrpPrice(ci.getMrpPrice())
                        .sellingPrice(ci.getSellingPrice())
                        .deliveryDate(order.getDeliveryDate())
                        .build();

                items.add(item);

                mrp += ci.getMrpPrice();
                selling += ci.getSellingPrice();
            }

            order.setOrderItems(items);

            order.setTotalMrpPrice(mrp);

            order.setTotalSellingPrice(selling);

            order.setDiscount(mrp - selling);

            order.setTotalItems(items.size());

            orders.add(
                    orderRepo.save(order)
            );
        }

        cart.getCartItems().clear();

        cart.setTotalItems(0);
        cart.setTotalQuantity(0);

        cart.setTotalMrpPrice(0);
        cart.setTotalSellingPrice(0);

        cart.setCouponCode(null);
        cart.setCouponDiscount(0);

        cartRepo.save(cart);

        return orders;
    }

    @Override
    public Order getById(Long id) {

        return orderRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found: " + id
                        )
                );
    }

    @Override
    public List<Order> getUserOrders(User user) {

        return orderRepo.findByUserId(user.getId());
    }

    @Override
    public List<Order> getSellerOrders(Seller seller) {

        return orderRepo.findBySellerId(seller.getId());
    }

    @Override
    public Order updateStatus(Long id,
                              OrderStatus status) {

        Order o = getById(id);

        o.setOrderStatus(status);

        if (status == OrderStatus.DELIVERED) {

            o.setDeliveryDate(
                    LocalDateTime.now()
            );
        }

        return orderRepo.save(o);
    }

    @Override
    public void cancel(Long id,
                       User user) {

        Order o = getById(id);

        if (!o.getUser().getId().equals(user.getId())) {
            throw new OrderException("Not authorized");
        }

        if (o.getOrderStatus()
                == OrderStatus.DELIVERED) {

            throw new OrderException(
                    "Cannot cancel delivered order"
            );
        }

        o.setOrderStatus(OrderStatus.CANCELLED);

        orderRepo.save(o);
    }
}