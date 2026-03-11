package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.DiningTable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DiningTableValidationService {
    private final AnnotationValidationService annotationValidationService;

    public DiningTableValidationService(AnnotationValidationService annotationValidationService) {
        this.annotationValidationService = annotationValidationService;
    }

    public Collection<ValidationError> validate(DiningTable table) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(table));
        errors.addAll(validateCapacity(table));
        return errors;
    }

    public Collection<ValidationError> validateCapacity(DiningTable table) {

        if (table.getCapacity() != null && table.getCapacity() > 0) {
            return List.of();
        }

        return List.of(new ValidationError("Capacity must be a number greater than 0"));
    }
}
