package com.example.s1mple_store.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.s1mple_store.dtos.Products.CreateProductDto;
import com.example.s1mple_store.dtos.Products.ProductDto;
import com.example.s1mple_store.dtos.Products.UpdateProductDto;
import com.example.s1mple_store.exceptions.ApiError;
import com.example.s1mple_store.exceptions.ResourceNotFoundException;
import com.example.s1mple_store.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Validated
@CrossOrigin(origins = "http://dm0110.com")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Handle GET requests to retrieve all products.
     * 
     * @return ResponseEntity containing a list of ProductDto
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();

        // Add HATEOAS self link to each product
        for (ProductDto product : products) {
            Link selfLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).getProductById(product.getId()))
                    .withSelfRel();
            product.add(selfLink);
        }

        return ResponseEntity.ok(products);
    }

    /**
     * Handle GET requests to retrieve a single product by its ID.
     * 
     * @param id the ID of the product
     * @return ResponseEntity containing the ProductDto if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        ProductDto product = productService.getProductById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        // Add HATEOAS self link to the product
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).getProductById(id))
                .withSelfRel();
        product.add(selfLink);

        return ResponseEntity.ok(product);
    }

    /**
     * Handle POST requests to create a new product.
     * 
     * @param productDto the data transfer object containing the product details
     * @return ResponseEntity containing the created ProductDto
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);

        // Add HATEOAS self link to the created product
        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).getProductById(createdProduct.getId()))
                .withSelfRel();

        createdProduct.add(selfLink);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).getProductById(createdProduct.getId()))
                .toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    /**
     * Handle PUT requests to update an existing product by its ID.
     * 
     * @param id             the ID of the product to update
     * @param productDetails the data transfer object containing the updated product
     *                       details
     * @return ResponseEntity containing the updated ProductDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id,
            @Valid @RequestBody UpdateProductDto productDetails) {
        ProductDto updatedProduct = productService.updateProduct(id, productDetails);
        if (updatedProduct == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        // Add HATEOAS self link to the updated product
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).getProductById(id))
                .withSelfRel();
        updatedProduct.add(selfLink);

        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Handles DELETE requests to delete a product by its ID.
     * 
     * @param id the UUID of the product to be deleted
     * @return ResponseEntity with no content if the deletion is successful
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // #region ExceptionHandler

    /**
     * Handle validation exceptions thrown when request data fails validation.
     * 
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return ResponseEntity containing ApiError with HTTP status code 400 (Bad
     *         Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation Error", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    /**
     * Handle exceptions thrown when a requested resource is not found.
     * 
     * @param ex the ResourceNotFoundException containing the exception details
     * @return ResponseEntity containing ApiError with HTTP status code 404 (Not
     *         Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), "Resource Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Handle other exceptions that are not specifically handled.
     * 
     * @param ex the Exception containing the exception details
     * @return ResponseEntity containing ApiError with HTTP status code 500
     *         (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    // #endregion

}
