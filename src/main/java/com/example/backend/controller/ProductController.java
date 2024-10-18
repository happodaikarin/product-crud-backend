package com.example.backend.controller;

import com.example.backend.model.Product;
import com.example.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Clase interna para las respuestas de la API
    @Data
    @AllArgsConstructor
    private static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.createProduct(product);
            ApiResponse response = new ApiResponse(true, "Producto creado exitosamente", savedProduct);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, "Error al crear el producto", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            ApiResponse response = new ApiResponse(true, "Producto encontrado", productOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(false, "Producto no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails) {
        Optional<Product> updatedProductOpt = productService.updateProduct(id, productDetails);
        if (updatedProductOpt.isPresent()) {
            ApiResponse response = new ApiResponse(true, "Producto actualizado exitosamente", updatedProductOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(false, "Producto no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            ApiResponse response = new ApiResponse(true, "Producto eliminado exitosamente", null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(false, "Producto no encontrado", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Obtener productos por categoría
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getAllProducts().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
        return ResponseEntity.ok(products);
    }
}
