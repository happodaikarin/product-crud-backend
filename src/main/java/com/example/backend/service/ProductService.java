package com.example.backend.service;

import com.example.backend.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Optional<Product> updateProduct(Long id, Product product);
    boolean deleteProduct(Long id);
}
