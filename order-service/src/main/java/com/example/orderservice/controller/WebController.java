package com.example.orderservice.controller;

import com.example.orderservice.model.Product;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.dto.RestPageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product-service.url}")
    private String productServiceUrl;

    @GetMapping("/")
    public String index(Model model) {
        try {
         
            String productsUrl = productServiceUrl + "/products?page=0&size=100";

            ResponseEntity<RestPageImpl<Product>> response = restTemplate.exchange(
    productsUrl,
    HttpMethod.GET,
    null,
    new ParameterizedTypeReference<RestPageImpl<Product>>() {}
);

List<Product> products = response.getBody().getContent();
model.addAttribute("products", products);

            List<Order> orders = orderService.getAllOrders();
            model.addAttribute("orders", orders);

        } catch (Exception e) {
            model.addAttribute("products", List.of());
            model.addAttribute("orders", List.of());
            model.addAttribute("error", "Erro ao carregar dados: " + e.getMessage());
            e.printStackTrace();
        }

        return "index";
    }
}
