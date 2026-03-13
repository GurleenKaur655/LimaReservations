package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Event;
import nbcc.resto.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DiningTableValidationService {
    private final AnnotationValidationService annotationValidationService;
    private final DiningTableRepository diningTableRepository;

    public DiningTableValidationService(AnnotationValidationService annotationValidationService, DiningTableRepository diningTableRepository) {
        this.annotationValidationService = annotationValidationService;
        this.diningTableRepository = diningTableRepository;
    }

    public Collection<ValidationError> validate(DiningTable table) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(table));
        errors.addAll(validateCapacity(table));
        errors.addAll(validateUniqueName(table));
        return errors;
    }

    public Collection<ValidationError> validateCapacity(DiningTable table) {

        if (table.getCapacity() != null && table.getCapacity() > 0) {
            return List.of();
        }

        return List.of(new ValidationError("Capacity must be a number greater than 0"));
    }

    public Collection<ValidationError> validateUniqueName(DiningTable table) {
        if (table.getName() == null || table.getName().isBlank()) {
            return List.of();
        }

        if (table.getId() != null && table.getId() > 0) {

            var optionalDBTable = diningTableRepository.get(table.getId());

            if (optionalDBTable.isPresent()) {
                var dbTable = optionalDBTable.get();

                if (table.getName().equalsIgnoreCase(dbTable.getName())) {
                    return List.of();
                }
            }
        }

        if (diningTableRepository.exists(table.getName())) {
            return List.of(new ValidationError("Name must be unique"));
        }

        return List.of();
    }
}
