package com.kata.service;

import com.kata.dto.OrderRequestDTO;
import com.kata.exception.TimeSlotUnavailableException;
import com.kata.model.DeliveryMode;
import com.kata.model.Order;
import com.kata.model.TimeSlot;
import com.kata.repository.OrderRepository;
import com.kata.repository.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private TimeSlotRepository timeSlotRepository;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        timeSlotRepository = mock(TimeSlotRepository.class);
        orderService = new OrderServiceImpl(orderRepository, timeSlotRepository);
    }

    @Test
    void shouldCreateOrderWhenTimeSlotAvailable() {
        // Arrange
        TimeSlot slot = new TimeSlot();
        slot.setId(1L);
        slot.setReserved(false);

        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerName("Alice");
        request.setTimeSlotId(1L);
        request.setDeliveryMode(DeliveryMode.DELIVERY);

        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(slot));

        // Act
        orderService.createOrder(request);

        // Assert
        assertTrue(slot.isReserved());
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(captor.capture());

        Order savedOrder = captor.getValue();
        assertEquals("Alice", savedOrder.getCustomerName());
        assertEquals(DeliveryMode.DELIVERY, savedOrder.getDeliveryMode());
        assertEquals(slot, savedOrder.getTimeSlot());
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotNotFound() {
        // Arrange
        OrderRequestDTO request = new OrderRequestDTO();
        request.setTimeSlotId(42L);

        when(timeSlotRepository.findById(42L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TimeSlotUnavailableException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotAlreadyReserved() {
        // Arrange
        TimeSlot slot = new TimeSlot();
        slot.setId(2L);
        slot.setReserved(true);

        OrderRequestDTO request = new OrderRequestDTO();
        request.setTimeSlotId(2L);

        when(timeSlotRepository.findById(2L)).thenReturn(Optional.of(slot));

        // Act & Assert
        assertThrows(TimeSlotUnavailableException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }
}