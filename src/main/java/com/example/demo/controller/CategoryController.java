package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.modals.Category;
import com.example.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService svc;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> all() {
        return ResponseEntity.ok(ApiResponse.success("OK", svc.getAll()));
    }

    @GetMapping("/root")
    public ResponseEntity<ApiResponse<List<Category>>> roots() {
        return ResponseEntity.ok(ApiResponse.success("OK", svc.getRoots()));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> create(@RequestBody Map<String, Object> body) {

        Long parentId = body.get("parentId") != null
                ? Long.valueOf(body.get("parentId").toString())
                : null;

        int level = body.get("level") != null
                ? Integer.parseInt(body.get("level").toString())
                : 1;

        return ResponseEntity.ok(ApiResponse.success("Created",
                svc.create(
                        (String) body.get("name"),
                        (String) body.get("description"),
                        (String) body.get("imageUrl"),
                        parentId,
                        level
                )));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted", "success"));
    }
}