package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.SeatingRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SeatingValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final SeatingRepository seatingRepository;

    public SeatingValidationService(AnnotationValidationService annotationValidationService, SeatingRepository seatingRepository) {
        this.annotationValidationService = annotationValidationService;
        this.seatingRepository = seatingRepository;
    }

    public Collection<ValidationError> validate(Seating seating) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(seating));
        errors.addAll(validateUniqueName(seating));
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
