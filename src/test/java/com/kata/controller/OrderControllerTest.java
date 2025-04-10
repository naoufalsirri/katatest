package com.kata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kata.dto.OrderRequestDTO;
import com.kata.model.DeliveryMode;
import com.kata.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {

        OrderRequestDTO orderRequest = new OrderRequestDTO();
        orderRequest.setCustomerName("Alice");
        orderRequest.setDeliveryMode(DeliveryMode.DELIVERY);
        orderRequest.setTimeSlotId(Long.valueOf(2));

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Commande enregistrée avec succès !"));

        // Verify the service was called once with the expected DTO
        Mockito.verify(orderService, times(1)).createOrder(Mockito.any(OrderRequestDTO.class));
    }
}