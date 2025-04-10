package com.kata.service;

import com.kata.dto.TimeSlotResponseDTO;
import com.kata.exception.TimeSlotUnavailableException;
import com.kata.mapper.TimeSlotMapper;
import com.kata.model.DeliveryMode;
import com.kata.model.TimeSlot;
import com.kata.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;
import reactor.core.publisher.Flux;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Cacheable("timeslot")
    public TimeSlotResponseDTO getTimeSlotById(Long timeSlotId) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new TimeSlotUnavailableException("Créneau non trouvé"));
        return TimeSlotMapper.toDto(timeSlot);
    }

    @Override
    public List<TimeSlotResponseDTO> getAvailableTimeSlots(DeliveryMode mode) {
        return timeSlotRepository.findByDeliveryModeAndIsReservedFalse(mode)
                .stream()
                .map(TimeSlotMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Flux<TimeSlotResponseDTO> getAvailableTimeSlotsNonBlocked() {
        List<TimeSlot> timeSlots = timeSlotRepository.findAll(); // appel bloquant
        return Flux.fromIterable(timeSlots)
                .map(TimeSlotMapper::toDto);
    }
}
