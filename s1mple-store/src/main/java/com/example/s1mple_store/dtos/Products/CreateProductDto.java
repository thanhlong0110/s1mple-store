package com.example.s1mple_store.dtos.Products;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CreateProductDto {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    private String description;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}