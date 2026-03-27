package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.ReservationValidationService;
import nbcc.resto.validation.SeatingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;
    private final ReservationValidationService reservationValidationService;


    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationValidationService reservationValidationService) {
        this.reservationRepository = reservationRepository;
        this.reservationValidationService = reservationValidationService;
    }

    @Override
    public Result<Collection<Reservation>> getAll() {
        try {
            return ValidationResults.success(reservationRepository.getAll());
        } catch (Exception e) {
            logger.error("Error getting all reservations", e);
            return ValidationResults.error();
        }
    }

    @Override
    public ValidatedResult<Reservation> get(Long id) {
        try {
            return ValidationResults.success(reservationRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Reservation> create(Reservation reservation) {
        try {
            reservation.setStatus(ReservationStatus.PENDING);
            reservation.setUuid(UUID.randomUUID());

            var errors = reservationValidationService.validate(reservation);

            if (errors.isEmpty()) {
                return ValidationResults.success(reservationRepository.create(reservation));
            } else {
                logger.debug("Validation errors for reservation create {}: {}", reservation, errors);
                return ValidationResults.invalid(reservation, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating reservation {}", reservation, e);
            return ValidationResults.error();
        }
    }
    @Override
    public Boolean existsBySeatingId(Long seatingId) {
        return reservationRepository.existsBySeatingId(seatingId);
    }
    @Override
    public Boolean existsBySeatingEventId(Long eventId) {
        return reservationRepository.existsBySeatingEventId(eventId);
    }

    @Override
    public ValidatedResult<Reservation> getByUuid(UUID uuid) {
        try {
            return ValidationResults.success(reservationRepository.getByUuid(uuid).orElse(null));
        } catch (Exception e) {
            logger.error("Error retrieving reservation with uuid: {}", uuid, e);
            return ValidationResults.error(e);
        }
    }
    @Override
    public Result<Collection<Reservation>> getAll(String eventName, ReservationStatus status) {
        try {
            return ValidationResults.success(reservationRepository.getAll(eventName, status));
        } catch (Exception e) {
            logger.error("Error retrieving reservations", e);
            return ValidationResults.error(e);
        }
    }
}
