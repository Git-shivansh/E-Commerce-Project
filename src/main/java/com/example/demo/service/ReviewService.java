package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.modals.Review;
import com.example.demo.modals.User;

public interface ReviewService {

    Review add(User user, ReviewRequest req);

    List<Review> getByProduct(Long productId);

    Review update(Long id, ReviewRequest req, User user);

    void delete(Long id, User user);
}