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
        if (reservation.getGroupSize() == null) return List.of();

        if (reservation.getGroupSize() < 1) {
            return List.of(new ValidationError("Group size must be at least 1."));
        }
        var seating = reservation.getSeating();
        var capacity = seating.getTotalCapacity();
        if (reservation.getGroupSize() > capacity) {
            return List.of(new ValidationError("Group size cannot exceed table capacity."));
        }
        return List.of();
    }

    public Collection<ValidationError> validateApprove(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            return List.of(new ValidationError("An approved reservation cannot be changed."));
        }

        if (reservationRepository.isSeatingAlreadyApproved(
                reservation.getSeating().getId(), reservation.getId())) {
            return List.of(new ValidationError(
                    "This seating is already assigned to another approved reservation."));
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
