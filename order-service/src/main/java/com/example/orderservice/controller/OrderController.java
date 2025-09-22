package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            UUID productId = UUID.fromString(orderRequest.get("productId").toString());
            Integer quantity = Integer.parseInt(orderRequest.get("quantity").toString());
            
            Order createdOrder = orderService.createOrder(productId, quantity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request data"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "order-service"));
    }
}