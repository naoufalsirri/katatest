package com.kata.repository;

import com.kata.model.DeliveryMode;
import com.kata.model.TimeSlot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByDeliveryModeAndIsReservedFalse(DeliveryMode deliveryMode);
}