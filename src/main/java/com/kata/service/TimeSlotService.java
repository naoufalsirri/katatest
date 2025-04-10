package com.kata.service;

import com.kata.dto.TimeSlotResponseDTO;
import com.kata.model.DeliveryMode;
import com.kata.model.TimeSlot;
import reactor.core.publisher.Flux;

import java.util.List;

public interface TimeSlotService {
    List<TimeSlotResponseDTO> getAvailableTimeSlots(DeliveryMode mode);
    TimeSlotResponseDTO getTimeSlotById(Long timeSlotId);

    Flux<TimeSlotResponseDTO> getAvailableTimeSlotsNonBlocked();
}
