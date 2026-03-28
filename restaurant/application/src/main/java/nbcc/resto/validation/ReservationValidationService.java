package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Reservation;
import nbcc.resto.dto.ReservationStatus;
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

    public ReservationValidationService(AnnotationValidationService annotationValidationService,
                                        ReservationRepository reservationRepository) {
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

    public Collection<ValidationError> validateApprove(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            return List.of(new ValidationError("An approved reservation cannot be changed."));
        }

        if (reservation.getSeating() == null || reservation.getSeating().getTable() == null) {
            return List.of(new ValidationError("This seating has no table assigned and cannot be approved."));
        }

        if (reservationRepository.isSeatingAlreadyApproved(reservation.getSeating().getId(), reservation.getId())) {
            return List.of(new ValidationError("This seating's table is already assigned to another approved reservation."));
        }

        return List.of();
    }

    public Collection<ValidationError> validateDeny(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            return List.of(new ValidationError("An approved reservation cannot be changed."));
        }

        return List.of();
    }
}
