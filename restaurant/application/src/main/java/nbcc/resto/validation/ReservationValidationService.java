package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Reservation;
import nbcc.resto.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

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
        return errors;
    }

}
