package com.kata.data;
import com.kata.model.DeliveryMode;
import com.kata.model.TimeSlot;
import com.kata.repository.TimeSlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TimeSlotRepository timeSlotRepository;

    public DataInitializer(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Création de quelques créneaux pour tester
        TimeSlot slot1 = new TimeSlot();
        slot1.setDate(LocalDate.now().plusDays(1));
        slot1.setStartTime(LocalTime.of(10, 0));
        slot1.setEndTime(LocalTime.of(12, 0));
        slot1.setDeliveryMode(DeliveryMode.DRIVE);
        slot1.setReserved(false);

        TimeSlot slot2 = new TimeSlot();
        slot2.setDate(LocalDate.now().plusDays(1));
        slot2.setStartTime(LocalTime.of(12, 0));
        slot2.setEndTime(LocalTime.of(14, 0));
        slot2.setDeliveryMode(DeliveryMode.DELIVERY);
        slot2.setReserved(false);

        TimeSlot slot3 = new TimeSlot();
        slot3.setDate(LocalDate.now().plusDays(2));
        slot3.setStartTime(LocalTime.of(9, 0));
        slot3.setEndTime(LocalTime.of(11, 0));
        slot3.setDeliveryMode(DeliveryMode.DELIVERY_TODAY);
        slot3.setReserved(false);

        TimeSlot slot4 = new TimeSlot();
        slot4.setDate(LocalDate.now().plusDays(2));
        slot4.setStartTime(LocalTime.of(14, 0));
        slot4.setEndTime(LocalTime.of(16, 0));
        slot4.setDeliveryMode(DeliveryMode.DELIVERY_ASAP);
        slot4.setReserved(false);

        // Sauvegarde dans la base de données
        timeSlotRepository.saveAll(java.util.List.of(slot1, slot2, slot3,slot4));
        System.out.println("Créneaux de livraison ajoutés avec succès !");
    }
}