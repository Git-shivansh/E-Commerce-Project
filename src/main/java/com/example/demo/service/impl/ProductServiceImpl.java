package com.example.demo.service.impl;

import com.example.demo.dto.ProductRequest;
import com.example.demo.exception.ProductException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modal.Category;
import com.example.demo.modal.Product;
import com.example.demo.modal.Seller;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Override
    public Product createProduct(ProductRequest req, Seller seller) {

        Category cat = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        int disc = req.getMrpPrice() > 0
                ? (int) (((req.getMrpPrice() - req.getSellingPrice())
                / req.getMrpPrice()) * 100)
                : 0;

        Product product = Product.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .mrpPrice(req.getMrpPrice())
                .sellingPrice(req.getSellingPrice())
                .discountPercent(disc)
                .quantity(req.getQuantity())
                .color(req.getColor())
                .sizes(req.getSizes())
                .images(req.getImages())
                .category(cat)
                .seller(seller)
                .build();

        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(Long id,
                                 ProductRequest req,
                                 Seller seller) {

        Product p = getById(id);

        if (!p.getSeller().getId().equals(seller.getId())) {
            throw new ProductException("Not authorized");
        }

        if (req.getTitle() != null) {
            p.setTitle(req.getTitle());
        }

        if (req.getDescription() != null) {
            p.setDescription(req.getDescription());
        }

        if (req.getMrpPrice() > 0) {
            p.setMrpPrice(req.getMrpPrice());
        }

        if (req.getSellingPrice() > 0) {
            p.setSellingPrice(req.getSellingPrice());
        }

        if (req.getQuantity() > 0) {
            p.setQuantity(req.getQuantity());
        }

        if (req.getImages() != null) {
            p.setImages(req.getImages());
        }

        if (req.getCategoryId() != null) {

            Category cat = categoryRepo.findById(req.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category not found")
                    );

            p.setCategory(cat);
        }

        return productRepo.save(p);
    }

    @Override
    public void deleteProduct(Long id, Seller seller) {

        Product p = getById(id);

        if (!p.getSeller().getId().equals(seller.getId())) {
            throw new ProductException("Not authorized");
        }

        p.setActive(false);

        productRepo.save(p);
    }

    @Override
    public Product getById(Long id) {

        return productRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found: " + id
                        )
                );
    }

    @Override
    public List<Product> getAll() {
        return productRepo.findByIsActiveTrue();
    }

    @Override
    public List<Product> getByCategory(Long categoryId) {
        return productRepo.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> search(String keyword) {
        return productRepo.findByTitleContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> getBySeller(Long sellerId) {
        return productRepo.findBySellerId(sellerId);
    }
}