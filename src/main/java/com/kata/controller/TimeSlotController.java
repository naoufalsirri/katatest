package com.kata.controller;

import com.kata.config.DeliveryApiProperties;
import com.kata.dto.TimeSlotResponseDTO;
import com.kata.model.DeliveryMode;
import com.kata.service.TimeSlotService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;



@RestController
@RequestMapping("/api/timeslots")
@CrossOrigin
@Tag(name = "Créneaux", description = "Gestion des créneaux de livraison")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;
    private final DeliveryApiProperties deliveryApiProperties;

    public TimeSlotController(TimeSlotService timeSlotService, DeliveryApiProperties deliveryApiProperties) {
        this.timeSlotService = timeSlotService;
        this.deliveryApiProperties = deliveryApiProperties;
    }

    /**
     * Récupérer les créneaux disponibles pour un mode de livraison donné
     * Exemple : GET /api/timeslots?mode=DELIVERY
     */
    @Operation(summary = "Obtenir les créneaux disponibles", description = "Récupère tous les créneaux non réservés pour un mode de livraison donné.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @GetMapping
    public List<EntityModel<TimeSlotResponseDTO>> getAvailableTimeSlots(@RequestParam("mode") DeliveryMode mode) {
        List<TimeSlotResponseDTO> timeSlots = timeSlotService.getAvailableTimeSlots(mode);
        return timeSlots.stream()
                .map(this::addLinks)
                .collect(Collectors.toList());
    }


    @Operation(summary = "Récupérer un créneau par son ID", description = "Retourne un créneau spécifique si disponible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Créneau récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Créneau non trouvé")
    })
    @GetMapping("/timeslot")
    public TimeSlotResponseDTO getTimeSlot(@RequestParam("id") String id) {
        return timeSlotService.getTimeSlotById(Long.valueOf(id));
    }

    @Operation(summary = "Récupérer les créneaux disponibles (non-bloquant)", description = "Retourne la liste des créneaux non réservés en mode réactif (Flux).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    @GetMapping("/nonblocked")
    public Flux<TimeSlotResponseDTO> getAvailableTimeSlotsNonBlocked() {
        return timeSlotService.getAvailableTimeSlotsNonBlocked();
    }

    @Operation(summary = "Stream des créneaux disponibles", description = "Retourne les créneaux disponibles via Server-Sent Events (SSE).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream démarré avec succès"),
            @ApiResponse(responseCode = "503", description = "Le mode streaming n'est pas activé")
    })
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TimeSlotResponseDTO> streamAvailableTimeSlots(@RequestParam("mode") DeliveryMode mode) {
        if (!deliveryApiProperties.isStreamingEnabled()) {
            return Flux.error(new IllegalStateException("Le mode streaming n'est pas activé"));
        }

        return timeSlotService.getAvailableTimeSlotsNonBlocked()
                .delayElements(Duration.ofMillis(500)); // Simule un envoi progressif
    }


    private EntityModel<TimeSlotResponseDTO> addLinks(TimeSlotResponseDTO timeSlotResponseDTO) {
        EntityModel<TimeSlotResponseDTO> resource = EntityModel.of(timeSlotResponseDTO);

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TimeSlotController.class)
                        .getAvailableTimeSlots(timeSlotResponseDTO.getDeliveryMode()))
                .withSelfRel();
        resource.add(selfLink);

        Link homeLink = WebMvcLinkBuilder.linkTo(TimeSlotController.class).withRel("home");
        resource.add(homeLink);

        return resource;
    }
}
