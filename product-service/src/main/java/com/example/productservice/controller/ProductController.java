package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import com.example.productservice.dto.RestPageImpl; 
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

   
  @GetMapping
public ResponseEntity<RestPageImpl<Product>> getProducts(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size);
    Page<Product> products = productService.getProducts(search, pageable);

    RestPageImpl<Product> restPage = new RestPageImpl<>(
            products.getContent(),
            PageRequest.of(products.getNumber(), products.getSize()),
            products.getTotalElements()
    );

    return ResponseEntity.ok(restPage);
}


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable UUID id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(
            @PathVariable UUID id, 
            @RequestBody Map<String, Integer> stockUpdate) {
        try {
            Integer newStock = stockUpdate.get("stock");
            if (newStock == null || newStock < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid stock value"));
            }
            
            Product updatedProduct = productService.updateStock(id, newStock);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "product-service"));
    }
}
