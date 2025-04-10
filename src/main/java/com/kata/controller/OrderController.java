package com.kata.controller;

import com.kata.dto.OrderRequestDTO;
import com.kata.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO request) {
        System.out.println(request.getCustomerName());
        orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Commande enregistrée avec succès !");
    }
}