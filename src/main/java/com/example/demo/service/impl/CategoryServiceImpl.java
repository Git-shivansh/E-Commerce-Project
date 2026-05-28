package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.modal.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Override
    public Category create(String name,
                           String description,
                           String imageUrl,
                           Long parentId,
                           int level) {

        Category cat = Category.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .level(level)
                .build();

        if (parentId != null) {

            cat.setParentCategory(
                    getById(parentId)
            );
        }

        return repo.save(cat);
    }

    @Override
    public Category getById(Long id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found: " + id
                        )
                );
    }

    @Override
    public List<Category> getAll() {

        return repo.findAll();
    }

    @Override
    public List<Category> getRoots() {

        return repo.findByParentCategoryIsNull();
    }

    @Override
    public void delete(Long id) {

        repo.deleteById(id);
    }
}