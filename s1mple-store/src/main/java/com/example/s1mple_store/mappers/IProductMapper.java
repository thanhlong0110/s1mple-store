package com.example.s1mple_store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.example.s1mple_store.dtos.Products.CreateProductDto;
import com.example.s1mple_store.dtos.Products.ProductDto;
import com.example.s1mple_store.dtos.Products.UpdateProductDto;
import com.example.s1mple_store.models.Product;

@Mapper
public interface IProductMapper {
    
    IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);

    ProductDto toProductDto(Product product);

    Product toProduct(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    Product toProduct(CreateProductDto createProductDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    void updateProductFromDto(UpdateProductDto dto, @MappingTarget Product existingProduct);
}
