package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ProductRequest;
import com.example.demo.modals.Product;
import com.example.demo.modals.Seller;
import com.example.demo.service.AuthService;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AuthService auth;

    @GetMapping("/api/products/public/all")
    public ResponseEntity<ApiResponse<List<Product>>> all() {
        return ResponseEntity.ok(ApiResponse.success("OK", productService.getAll()));
    }

    @GetMapping("/api/products/public/{id}")
    public ResponseEntity<ApiResponse<Product>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("OK", productService.getById(id)));
    }

    @GetMapping("/api/products/public/search")
    public ResponseEntity<ApiResponse<List<Product>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success("OK", productService.search(keyword)));
    }

    @GetMapping("/api/products/public/category/{cid}")
    public ResponseEntity<ApiResponse<List<Product>>> byCategory(@PathVariable Long cid) {
        return ResponseEntity.ok(ApiResponse.success("OK", productService.getByCategory(cid)));
    }

    @PostMapping("/api/seller/products")
    public ResponseEntity<ApiResponse<Product>> create(
            @RequestHeader("Authorization") String jwt,
            @RequestBody ProductRequest req) {

        Seller seller = auth.getSellerFromToken(jwt.substring(7));
        return ResponseEntity.ok(ApiResponse.success("Created",
                productService.createProduct(req, seller)));
    }

    @PutMapping("/api/seller/products/{id}")
    public ResponseEntity<ApiResponse<Product>> update(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id,
            @RequestBody ProductRequest req) {

        Seller seller = auth.getSellerFromToken(jwt.substring(7));
        return ResponseEntity.ok(ApiResponse.success("Updated",
                productService.updateProduct(id, req, seller)));
    }

    @DeleteMapping("/api/seller/products/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) {

        Seller seller = auth.getSellerFromToken(jwt.substring(7));
        productService.deleteProduct(id, seller);

        return ResponseEntity.ok(ApiResponse.success("Deleted", "success"));
    }

    @GetMapping("/api/seller/products")
    public ResponseEntity<ApiResponse<List<Product>>> sellerProducts(
            @RequestHeader("Authorization") String jwt) {

        Seller seller = auth.getSellerFromToken(jwt.substring(7));
        return ResponseEntity.ok(ApiResponse.success("OK",
                productService.getBySeller(seller.getId())));
    }
}