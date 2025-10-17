package com.pawradise.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto {
    private String name;

    private String sku;

    private String description;

    private String category;

    private String brand;

    private double price;

    private int stockQuantity;

    private List<String> featuredSpecies =  new ArrayList<>();
}