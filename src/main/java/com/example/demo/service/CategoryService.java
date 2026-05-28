package com.example.demo.service;

import com.example.demo.modal.Category;

import java.util.List;

public interface CategoryService {

    Category create(String name,
                    String description,
                    String imageUrl,
                    Long parentId,
                    int level);

    Category getById(Long id);

    List<Category> getAll();

    List<Category> getRoots();

    void delete(Long id);
}