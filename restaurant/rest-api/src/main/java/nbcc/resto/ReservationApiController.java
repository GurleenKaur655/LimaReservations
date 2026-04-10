package nbcc.resto;

import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.resto.dto.Reservation;
import nbcc.resto.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static nbcc.resto.result.ResultHandler.handleResult;

@Tag(name = "Reservation API")
@RestController
@RequestMapping("/api/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Result<Collection<Reservation>>> getApprovedReservations(@PathVariable Long eventId) {

        var result = reservationService.getApprovedByEventId(eventId);
        return handleResult(result, HttpStatus.OK);
    }

}
