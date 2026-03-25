package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.SeatingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class SeatingServiceImpl implements SeatingService {

    private final Logger logger = LoggerFactory.getLogger(SeatingServiceImpl.class);

    private final SeatingRepository seatingRepository;
    private final SeatingValidationService  validationService;
    private final EventService eventService;
    private final ReservationService reservationService;

    public SeatingServiceImpl(SeatingRepository seatingRepository, SeatingValidationService seatingValidationService, EventService eventService, ReservationService reservationService) {
        this.seatingRepository = seatingRepository;
        this.validationService = seatingValidationService;
        this.eventService = eventService;
        this.reservationService = reservationService;
    }

    @Override
    public Result<Collection<Seating>> getAll() {
        try {
            return ValidationResults.success(seatingRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all seating", e);
            return ValidationResults.error(e);
        }
    }
    @Override
    public ValidatedResult<Collection<Seating>> getAll(Long eventId) {
        try {
            return ValidationResults.success(seatingRepository.getAll(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving all songs for event with id: {}", eventId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> get(Long id) {
       try{
           return ValidationResults.success(seatingRepository.get(id));
       } catch (Exception e) {
           logger.error("Error retrieving seating {}", id, e);
           return ValidationResults.error(e);
       }
    }

    @Override
    public ValidatedResult<Seating> create(Seating seating) {
        try {

            if (seating.getEvent() != null && seating.getEvent().getId() != null) {
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

    @Override
    public ValidatedResult<Seating> update(Seating seating) {
        try {

            if (seating.getEvent() != null && seating.getEvent().getId() != null) {
                var eventResult = eventService.get(seating.getEvent().getId());
                var event = eventResult.getValue();
                seating.setEvent(event);

                if (seating.getDurationMinutes() == null) {
                    seating.setDurationMinutes(event.getDurationMinutes());
                }
            }

            var errors = validationService.validate(seating);

            if (errors.isEmpty()) {
                return ValidationResults.success(seatingRepository.update(seating));
            } else {
                logger.debug("Validation errors for seating create {}: {}", seating, errors);
                return ValidationResults.invalid(seating, errors);
            }
        } catch (Exception e) {
            logger.error("Error updating seating {}", seating, e);
            return ValidationResults.error(seating);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            var result = seatingRepository.get(id);
            if (result.isPresent()) {
                var seating = result.get();
                if () {
                    seatingRepository.archive(id);
                    logger.debug("Past event with id {} archived instead of deleted", id);
                } else {
                    seatingRepository.delete(id);
                    logger.debug("Event with id {} deleted", id);
                }
            }
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> archive(Long id) {
        try{
            var result = seatingRepository.get(id);
            if (result.isEmpty()) {
                return ValidationResults.invalid(null, "Seating not found");
            }

            seatingRepository.archive(id);
            logger.debug("Event with id {} archived", id);
            return ValidationResults.success();
        }catch (Exception e) {
            logger.error("Error archiving event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }


}
