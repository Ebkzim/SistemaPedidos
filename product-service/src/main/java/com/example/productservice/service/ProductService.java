package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists");
        }
        return productRepository.save(product);
    }

    public Page<Product> getProducts(String search, Pageable pageable) {
        return productRepository.findBySearchTerm(search, pageable);
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Product updateStock(UUID id, Integer newStock) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
        
        Product product = productOpt.get();
        product.setStock(newStock);
        return productRepository.save(product);
    }

    public boolean reduceStock(UUID id, Integer quantity) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return false;
        }
        
        Product product = productOpt.get();
        if (product.getStock() < quantity) {
            return false;
        }
        
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return true;
    }
}