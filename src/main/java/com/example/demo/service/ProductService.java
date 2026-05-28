package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.modal.Product;
import com.example.demo.modal.Seller;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductRequest req, Seller seller);

    Product updateProduct(Long id, ProductRequest req, Seller seller);

    void deleteProduct(Long id, Seller seller);

    Product getById(Long id);

    List<Product> getAll();

    List<Product> getByCategory(Long categoryId);

    List<Product> search(String keyword);

    List<Product> getBySeller(Long sellerId);
}