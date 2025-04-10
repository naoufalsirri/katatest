package com.kata.service;

import com.kata.dto.OrderRequestDTO;
import com.kata.exception.TimeSlotUnavailableException;
import com.kata.model.Order;
import com.kata.model.TimeSlot;
import com.kata.repository.OrderRepository;
import com.kata.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TimeSlotRepository timeSlotRepository;

    public OrderServiceImpl(OrderRepository orderRepository, TimeSlotRepository timeSlotRepository) {
        this.orderRepository = orderRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public void createOrder(OrderRequestDTO request) {
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new TimeSlotUnavailableException("Créneau non trouvé"));

        if (timeSlot.isReserved()) {
            throw new TimeSlotUnavailableException("Ce créneau est déjà réservé.");
        }

        timeSlot.setReserved(true);
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setDeliveryMode(request.getDeliveryMode());
        order.setTimeSlot(timeSlot);

        orderRepository.save(order);
    }
}