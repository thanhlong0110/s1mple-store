package com.example.s1mple_store.dtos.Products;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDto extends RepresentationModel<ProductDto>{
    private UUID id;

    @NotBlank(message = "Product name can't be blank.")
    @Size(min = 2, max = 250, message = "Name must be between 2 and 50 characters.")
    private String name;

    @NotNull(message = "Price is mandatory.")
    @Min(value = 0, message = "Price must be positve.")
    private BigDecimal price;

    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
