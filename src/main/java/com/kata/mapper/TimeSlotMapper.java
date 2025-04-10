package com.kata.mapper;
import com.kata.dto.TimeSlotResponseDTO;
import com.kata.model.TimeSlot;

public class TimeSlotMapper {

    public static TimeSlotResponseDTO toDto(TimeSlot entity) {
        TimeSlotResponseDTO dto = new TimeSlotResponseDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setDeliveryMode(entity.getDeliveryMode());
        dto.setReserved(entity.isReserved());
        return dto;
    }

    public static TimeSlot toEntity(TimeSlotResponseDTO dto) {
            TimeSlot entity = new TimeSlot();
            entity.setId(dto.getId());
            entity.setDate(dto.getDate());
            entity.setStartTime(dto.getStartTime());
            entity.setEndTime(dto.getEndTime());
            entity.setDeliveryMode(dto.getDeliveryMode());
            entity.setReserved(dto.isReserved());

        return entity;
    }
}
