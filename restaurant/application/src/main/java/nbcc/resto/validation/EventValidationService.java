package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Event;
import nbcc.resto.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EventValidationService {
    private final EventRepository eventRepository;
    private final AnnotationValidationService annotationValidationService;

    public EventValidationService(AnnotationValidationService annotationValidationService, EventRepository eventRepository) {
        this.annotationValidationService = annotationValidationService;
        this.eventRepository = eventRepository;
    }

    public Collection<ValidationError> validate(Event event) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(event));
        errors.addAll(validateUniqueName(event));
        return errors;
    }


    public Collection<ValidationError> validateUniqueName(Event event) {
        if (event.getName() == null || event.getName().isBlank()) {
            return List.of();
        }

        if (event.getId() != null && event.getId() > 0) {

            var optionalDBEvent = eventRepository.get(event.getId());

            if (optionalDBEvent.isPresent()) {
                var dbEvent = optionalDBEvent.get();

                if (event.getName().equalsIgnoreCase(dbEvent.getName())) {
                    return List.of();
                }
            }
        }

        if (eventRepository.exists(event.getName())) {
            return List.of(new ValidationError("Name must be unique"));
        }

        return List.of();
    }
}
