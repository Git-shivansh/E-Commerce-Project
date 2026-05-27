package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ReviewRequest;
import com.example.demo.modal.Review;
import com.example.demo.service.AuthService;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService svc;
    private final AuthService auth;

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> add(
            @RequestHeader("Authorization") String jwt,
            @RequestBody ReviewRequest req
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Added",
                        svc.add(
                                auth.getUserFromToken(jwt.substring(7)),
                                req
                        )
                )
        );
    }

    @GetMapping("/product/{pid}")
    public ResponseEntity<ApiResponse<List<Review>>> byProduct(
            @PathVariable Long pid
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "OK",
                        svc.getByProduct(pid)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Review>> update(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id,
            @RequestBody ReviewRequest req
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Updated",
                        svc.update(
                                id,
                                req,
                                auth.getUserFromToken(jwt.substring(7))
                        )
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) {

        svc.delete(
                id,
                auth.getUserFromToken(jwt.substring(7))
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Deleted",
                        "success"
                )
        );
    }
}