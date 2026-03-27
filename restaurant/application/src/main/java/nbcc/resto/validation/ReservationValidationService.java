package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ReservationValidationService {
    private final AnnotationValidationService annotationValidationService;
    private final ReservationRepository reservationRepository;

    public ReservationValidationService(AnnotationValidationService annotationValidationService, ReservationRepository reservationRepository) {
        this.annotationValidationService = annotationValidationService;
        this.reservationRepository = reservationRepository;
    }

    public Collection<ValidationError> validate(Reservation reservation) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(reservation));
        errors.addAll(validateGroupSize(reservation));
        return errors;
    }
    public Collection<ValidationError> validateGroupSize(Reservation reservation) {
        if (reservation.getGroupSize() == null) {
            return List.of();
        }
        var seating = reservation.getSeating();
        if (seating == null || seating.getTable() == null || seating.getTable().getCapacity() == null) {
            return List.of();
        }

        if (reservation.getGroupSize() > seating.getTable().getCapacity()) {
            return List.of(new ValidationError("Group size cannot exceed table capacity."));
        }

        return List.of();
    }

}
