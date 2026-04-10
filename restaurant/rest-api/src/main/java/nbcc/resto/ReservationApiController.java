package nbcc.resto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationRequest;
import nbcc.resto.service.EventService;
import nbcc.resto.service.ReservationService;
import nbcc.resto.service.SeatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static nbcc.resto.result.ResultHandler.handleResult;

@Tag(name = "Reservation API")
@RestController
@RequestMapping("/api/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;
    private final SeatingService seatingService;
    private final EventService eventService;

    public ReservationApiController(ReservationService reservationService,
                                    SeatingService seatingService, EventService eventService) {
        this.reservationService = reservationService;
        this.seatingService = seatingService;
        this.eventService = eventService;
    }

    @Operation(summary = "Get approved reservations by event")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Result<Collection<Reservation>>> getApprovedReservations(@PathVariable Long eventId) {
        var result = reservationService.getApprovedByEventId(eventId);
        return handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Create a reservation request")
    @PostMapping
    public ResponseEntity<Reservation> create(@Valid @RequestBody ReservationRequest request) {

        // Validate event
        var eventResult = eventService.get(request.getEventId());
        if (eventResult.isError() || eventResult.isEmpty()) {
            throw new IllegalArgumentException("Event not found for the provided EventId");
        }

        // Validate seating
        var seatingResult = seatingService.get(request.getSeatingId());
        if (seatingResult.isError() || seatingResult.isEmpty()) {
            throw new IllegalArgumentException("Seating not found for the provided SeatingId");
        }

        var seating = seatingResult.getValue();

        // Validate seating belongs to the specified event
        if (seating.getEvent() == null || !seating.getEvent().getId().equals(request.getEventId())) {
            throw new IllegalArgumentException("The provided SeatingId does not belong to the specified EventId");
        }

        var reservation = new Reservation()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setEmail(request.getEmail())
                .setGroupSize(request.getGroupSize())
                .setSeating(seating);

        var result = reservationService.create(reservation);

        if (result.isInvalid()) {
            throw new IllegalArgumentException(
                    result.getValidationErrors().iterator().next().getMessage()
            );
        }

        if (result.isError()) {
            throw new RuntimeException("Unexpected error creating reservation");
        }

        return new ResponseEntity<>(result.getValue(), HttpStatus.CREATED);
    }
}