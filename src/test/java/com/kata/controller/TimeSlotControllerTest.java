package com.kata.controller;

import com.kata.config.DeliveryApiProperties;
import com.kata.dto.TimeSlotResponseDTO;
import com.kata.model.DeliveryMode;
import com.kata.service.TimeSlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(TimeSlotController.class)
class TimeSlotControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TimeSlotService timeSlotService;

    @MockBean
    private DeliveryApiProperties deliveryApiProperties;

    private TimeSlotResponseDTO slot1;
    private TimeSlotResponseDTO slot2;

    @BeforeEach
    void setup() {
        slot1 = new TimeSlotResponseDTO();
        slot1.setId(1L);
        slot1.setDeliveryMode(DeliveryMode.DELIVERY);

        slot2 = new TimeSlotResponseDTO();
        slot2.setId(2L);
        slot2.setDeliveryMode(DeliveryMode.DELIVERY);
    }

    @Test
    void shouldReturnAvailableTimeSlots() {
        List<TimeSlotResponseDTO> slots = Arrays.asList(slot1, slot2);
        when(timeSlotService.getAvailableTimeSlots(DeliveryMode.DELIVERY)).thenReturn(slots);

        webTestClient.get()
                .uri("/api/timeslots?mode=DELIVERY")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class) // hateoas.EntityModel → generic object
                .hasSize(2);
    }

    @Test
    void shouldReturnTimeSlotById() {
        when(timeSlotService.getTimeSlotById(1L)).thenReturn(slot1);

        webTestClient.get()
                .uri("/api/timeslots/timeslot?id=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void shouldReturnNonBlockedTimeSlots() {
        when(timeSlotService.getAvailableTimeSlotsNonBlocked())
                .thenReturn(Flux.just(slot1, slot2));

        webTestClient.get()
                .uri("/api/timeslots/nonblocked")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TimeSlotResponseDTO.class)
                .hasSize(2);
    }

    @Test
    void shouldStreamTimeSlots_WhenStreamingEnabled() {
        when(deliveryApiProperties.isStreamingEnabled()).thenReturn(true);
        when(timeSlotService.getAvailableTimeSlotsNonBlocked())
                .thenReturn(Flux.just(slot1, slot2));

        webTestClient.get()
                .uri("/api/timeslots/stream?mode=DELIVERY")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TimeSlotResponseDTO.class)
                .hasSize(2);
    }

    @Test
    void shouldReturnError_WhenStreamingDisabled() {
        when(deliveryApiProperties.isStreamingEnabled()).thenReturn(false);

        webTestClient.get()
                .uri("/api/timeslots/stream?mode=DELIVERY")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}