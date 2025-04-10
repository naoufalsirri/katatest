package com.kata.dto;

import com.kata.model.DeliveryMode;

import java.time.LocalDate;
import java.time.LocalTime;
import io.swagger.v3.oas.annotations.media.Schema;

public class TimeSlotResponseDTO {



    @Schema(description = "ID du créneau", example = "1")
    private Long id;

    @Schema(description = "Date du jour", example = "2025-04-10")
    private LocalDate date;

    @Schema(description = "Début du créneau", example = "14:00:00")
    private LocalTime startTime;

    @Schema(description = "Fin du créneau", example = "15:00:00")
    private LocalTime endTime;

    @Schema(description = "Mode de livraison associé", example = "DELIVERY")
    private DeliveryMode deliveryMode;

    @Schema(description = "Check de reservation", example = "false")
    private boolean reserved;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
