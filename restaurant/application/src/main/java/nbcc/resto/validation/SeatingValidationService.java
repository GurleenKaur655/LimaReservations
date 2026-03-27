package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.service.EventService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SeatingValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final SeatingRepository seatingRepository;
    private final EventService eventService;

    public SeatingValidationService(AnnotationValidationService annotationValidationService, SeatingRepository seatingRepository, EventService eventService) {
        this.annotationValidationService = annotationValidationService;
        this.seatingRepository = seatingRepository;
        this.eventService = eventService;
    }

    public Collection<ValidationError> validate(Seating seating) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(seating));
        errors.addAll(validateUniqueName(seating));
        errors.addAll(validateDuration(seating));
        errors.addAll(validateStartDateWithinEvent(seating));
        errors.addAll(validateTableAvailability(seating));
        return errors;
    }

    public Collection<ValidationError> validateUniqueName(Seating seating) {
        if (seating.getName() == null || seating.getName().isBlank()) {
            return List.of();
        }

        if (seating.getId() != null && seating.getId() > 0) {

            var optionalDBSeating = seatingRepository.get(seating.getId());

            if (optionalDBSeating.isPresent()) {
                var dbSeating = optionalDBSeating.get();

                if (seating.getName().equalsIgnoreCase(dbSeating.getName())) {
                    return List.of();
                }
            }
        }

        if (seatingRepository.exists(seating.getName())) {
            return List.of(new ValidationError("Name must be unique"));
        }

        return List.of();
    }

    public Collection<ValidationError> validateDuration(Seating seating) {
        if (seating.getDurationMinutes() == null) {
            return List.of();
        }
        var eventId = eventService.get(seating.getEvent().getId());
        var eventDuration = eventId.getValue().getDurationMinutes();

        if (seating.getDurationMinutes() >  eventDuration) {
            return List.of(new ValidationError("Duration cannot be greater than Event Duration"));

        }

        return List.of();
    }

    public Collection<ValidationError> validateStartDateWithinEvent(Seating seating) {
        if (seating.getStartDateTime() == null || seating.getEvent() == null || seating.getEvent().getId() == null) {
            return List.of();
        }

        var eventResult = eventService.get(seating.getEvent().getId());
        if (eventResult.isError() || eventResult.getValue() == null) {
            return List.of();
        }

        var event = eventResult.getValue();
        if (event.getStartDate() == null || event.getEndDate() == null) {
            return List.of();
        }

        var startDate = seating.getStartDateTime().toLocalDate();

        if (startDate.isBefore(event.getStartDate()) || startDate.isAfter(event.getEndDate())) {
            return List.of(new ValidationError("Start date must be between event start date "
                    + event.getStartDate() + " and end date " + event.getEndDate()));
        }

        return List.of();
    }

    public Collection<ValidationError> validateTableAvailability(Seating seating) {

        if (seating.getTable() == null ||
                seating.getStartDateTime() == null ||
                seating.getDurationMinutes() == null) {
            return List.of();
        }

        var start = seating.getStartDateTime();
        var end = start.plusMinutes(seating.getDurationMinutes());

        var existingSeatings = seatingRepository.getByTableId(seating.getTable().getId());

        for (var existing : existingSeatings) {

            if (seating.getId() == null || !seating.getId().equals(existing.getId())) {

                var existingStart = existing.getStartDateTime();
                var existingEnd = existingStart.plusMinutes(existing.getDurationMinutes());

                boolean overlap =
                        start.isBefore(existingEnd) &&
                                end.isAfter(existingStart);

                if (overlap) {
                    return List.of(new ValidationError("Table is already used during this time"));
                }
            }
        }

        return List.of();
    }


}
