package com.example.backend.service;

import com.example.backend.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "app.use-mysql", havingValue = "false")
@Slf4j
public class JsonProductService implements ProductService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String filePath = "src/main/resources/data/products.json";

    private List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Configurar ObjectMapper para manejar LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        File file = new File(filePath);
        if (file.exists()) {
            try {
                products = objectMapper.readValue(file, new TypeReference<List<Product>>() {});
                log.info("Cargados {} productos desde JSON.", products.size());
            } catch (IOException e) {
                log.error("Error al leer el archivo JSON: {}", e.getMessage());
                products = new ArrayList<>();
            }
        } else {
            products = new ArrayList<>();
            saveToFile();
            log.info("Archivo JSON creado y listo para usarse.");
        }
    }

    @Override
    public Product createProduct(Product product) {
        // Asignar un ID único
        Long newId = products.stream()
                .map(Product::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
        product.setId(newId);

        // Establecer createdAt y updatedAt
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        products.add(product);
        saveToFile();
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return products;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existingProductOpt = getProductById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());

            // Actualizar updatedAt
            existingProduct.setUpdatedAt(LocalDateTime.now());

            saveToFile();
            return Optional.of(existingProduct);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> productOpt = getProductById(id);
        if (productOpt.isPresent()) {
            products.remove(productOpt.get());
            saveToFile();
            return true;
        }
        return false;
    }

    private void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), products);
            log.info("Archivo JSON actualizado correctamente.");
        } catch (IOException e) {
            log.error("Error al escribir en el archivo JSON: {}", e.getMessage());
        }
    }
}
