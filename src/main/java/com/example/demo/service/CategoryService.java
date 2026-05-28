package com.example.demo.service;

import java.util.List;

import com.example.demo.modals.Category;

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