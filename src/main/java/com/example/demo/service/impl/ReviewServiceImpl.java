package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modals.Product;
import com.example.demo.modals.Review;
import com.example.demo.modals.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final ProductRepository productRepo;

    @Override
    public Review add(User user,
                      ReviewRequest req) {

        Product product = productRepo.findById(req.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );

        if (reviewRepo.existsByUserIdAndProductId(
                user.getId(),
                product.getId()
        )) {

            throw new RuntimeException(
                    "Already reviewed"
            );
        }

        Review r = Review.builder()
                .user(user)
                .product(product)
                .reviewText(req.getReviewText())
                .rating(req.getRating())
                .productImages(req.getProductImages())
                .build();

        reviewRepo.save(r);

        List<Review> all = reviewRepo.findByProductId(
                product.getId()
        );

        product.setAvgRating(
                all.stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0)
        );

        product.setNumReviews(
                all.size()
        );

        productRepo.save(product);

        return r;
    }

    @Override
    public List<Review> getByProduct(Long productId) {

        return reviewRepo.findByProductId(productId);
    }

    @Override
    public Review update(Long id,
                         ReviewRequest req,
                         User user) {

        Review r = reviewRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found"
                        )
                );

        if (!r.getUser().getId().equals(user.getId())) {

            throw new RuntimeException(
                    "Not authorized"
            );
        }

        r.setReviewText(
                req.getReviewText()
        );

        r.setRating(
                req.getRating()
        );

        return reviewRepo.save(r);
    }

    @Override
    public void delete(Long id,
                       User user) {

        Review r = reviewRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found"
                        )
                );

        if (!r.getUser().getId().equals(user.getId())) {

            throw new RuntimeException(
                    "Not authorized"
            );
        }

        reviewRepo.delete(r);
    }
}