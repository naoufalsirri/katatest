package com.kata.service;

import com.kata.dto.OrderRequestDTO;

public interface OrderService {
    void createOrder(OrderRequestDTO request);
}
