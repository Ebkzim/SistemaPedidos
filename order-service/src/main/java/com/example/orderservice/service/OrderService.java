package com.example.orderservice.service;

import com.example.orderservice.model.Product;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product-service.url}")
    private String productServiceUrl;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Product getProduct(UUID productId) {
        try {
            String url = productServiceUrl + "/products/" + productId;
            return restTemplate.getForObject(url, Product.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Order createOrder(UUID productId, Integer quantity) {
       
        Product product = getProduct(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

       
        try {
            String url = productServiceUrl + "/products/" + productId + "/stock";
            Map<String, Integer> stockUpdate = Map.of("stock", product.getStock() - quantity);
            restTemplate.patchForObject(url, stockUpdate, Void.class); // ou null
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update stock");
        }

       
        Order order = new Order(productId, quantity);
        order.setStatus("CONFIRMED");
        return orderRepository.save(order);
    }
}
