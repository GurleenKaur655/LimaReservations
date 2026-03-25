package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.Reservation;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.ReservationValidationService;
import nbcc.resto.validation.SeatingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final ReservationValidationService reservationValidationService;
    private final SeatingService seatingService;


    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationValidationService reservationValidationService, SeatingService seatingService) {
        this.reservationRepository = reservationRepository;
        this.reservationValidationService = reservationValidationService;
        this.seatingService = seatingService;
    }

    @Override
    public Result<Collection<Reservation>> getAll() {
        return null;
    }

    @Override
    public ValidatedResult<Reservation> get(Long id) {
        return null;
    }

    @Override
    public ValidatedResult<Reservation> create(Reservation reservation) {
        try {

            if (.getEvent() != null && seating.getEvent().getId() != null) {
                var eventResult = eventService.get(seating.getEvent().getId());
                var event = eventResult.getValue();
                seating.setEvent(event);

                if (seating.getDurationMinutes() == null) {
                    seating.setDurationMinutes(event.getDurationMinutes());
                }
            }

            var errors = validationService.validate(seating);

            if (errors.isEmpty()) {
                return ValidationResults.success(seatingRepository.create(seating));
            } else {
                logger.debug("Validation errors for seating create {}: {}", seating, errors);
                return ValidationResults.invalid(seating, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating seating {}", seating, e);
            return ValidationResults.error(seating);
        }
    }
}
