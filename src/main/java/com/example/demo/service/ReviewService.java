package com.example.demo.service;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.modal.Review;
import com.example.demo.modal.User;

import java.util.List;

public interface ReviewService {

    Review add(User user, ReviewRequest req);

    List<Review> getByProduct(Long productId);

    Review update(Long id, ReviewRequest req, User user);

    void delete(Long id, User user);
}