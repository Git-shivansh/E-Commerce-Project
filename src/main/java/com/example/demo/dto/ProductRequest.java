package com.example.demo.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String title;
    private String description;
    private String color;
    private String sizes;

    private double mrpPrice;
    private double sellingPrice;

    private int quantity;

    private List<String> images;

    private Long categoryId;
}