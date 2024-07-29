package com.example.s1mple_store.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.s1mple_store.models.Product;

public interface IProductRepository extends JpaRepository<Product, UUID> {

}
