package com.example.s1mple_store.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.s1mple_store.dtos.Products.CreateProductDto;
import com.example.s1mple_store.dtos.Products.ProductDto;
import com.example.s1mple_store.dtos.Products.UpdateProductDto;
import com.example.s1mple_store.mappers.IProductMapper;
import com.example.s1mple_store.models.Product;
import com.example.s1mple_store.repositories.IProductRepository;

import org.springframework.http.HttpStatus;

@Service
public class ProductService {
    
    // @Autowired
    // private ProductRepository productRepository;

    private final IProductRepository productRepository;
    private final IProductMapper productMapper;

    // @Autowired
    public ProductService(IProductRepository productRepository, IProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                        .map(productMapper::toProductDto)
                        .collect(Collectors.toList());
    }

    public ProductDto getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

        return productMapper.toProductDto(product);
    }

    public ProductDto createProduct(CreateProductDto createProductDto) {
        if (productRepository.findAll().stream()
                                        .anyMatch(p -> p.getName().equalsIgnoreCase(createProductDto.getName())))
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product with the same name already exists");

        Product product = productMapper.toProduct(createProductDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDto(savedProduct);
    }

    public ProductDto updateProduct(UUID updateProductId, UpdateProductDto updateProductDto) {
        Product existingProduct = productRepository.findById(updateProductId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        
        productMapper.updateProductFromDto(updateProductDto, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toProductDto(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }
}
