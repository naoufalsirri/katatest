package com.kata.service;

import com.kata.dto.TimeSlotResponseDTO;
import com.kata.exception.TimeSlotUnavailableException;
import com.kata.model.DeliveryMode;
import com.kata.model.TimeSlot;
import com.kata.repository.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimeSlotServiceImplTest {

    private TimeSlotRepository timeSlotRepository;
    private TimeSlotServiceImpl timeSlotService;

    @BeforeEach
    void setUp() {
        timeSlotRepository = mock(TimeSlotRepository.class);
        timeSlotService = new TimeSlotServiceImpl(timeSlotRepository);
    }

    @Test
    void shouldReturnTimeSlotDtoWhenFound() {
        TimeSlot slot = new TimeSlot();
        slot.setId(1L);
        slot.setReserved(false);
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(slot));

        TimeSlotResponseDTO dto = timeSlotService.getTimeSlotById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotNotFound() {
        when(timeSlotRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TimeSlotUnavailableException.class, () -> {
            timeSlotService.getTimeSlotById(99L);
        });
    }

    @Test
    void shouldReturnAvailableTimeSlotsByMode() {
        TimeSlot slot1 = new TimeSlot();
        slot1.setId(1L);
        slot1.setDeliveryMode(DeliveryMode.DELIVERY);
        slot1.setReserved(false);

        TimeSlot slot2 = new TimeSlot();
        slot2.setId(2L);
        slot2.setDeliveryMode(DeliveryMode.DELIVERY);
        slot2.setReserved(false);

        when(timeSlotRepository.findByDeliveryModeAndIsReservedFalse(DeliveryMode.DELIVERY))
                .thenReturn(Arrays.asList(slot1, slot2));

        List<TimeSlotResponseDTO> results = timeSlotService.getAvailableTimeSlots(DeliveryMode.DELIVERY);

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());
    }
}