package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Menu;
import nbcc.resto.repository.DiningTableRepository;
import nbcc.resto.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MenuValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final MenuRepository menuRepository;

    public MenuValidationService(AnnotationValidationService annotationValidationService, MenuRepository menuRepository) {
        this.annotationValidationService = annotationValidationService;
        this.menuRepository = menuRepository;
    }

    public Collection<ValidationError> validate(Menu menu) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(menu));
        errors.addAll(validateUniqueName(menu));
        return errors;
    }


    public Collection<ValidationError> validateUniqueName(Menu menu) {
        if (menu.getName() == null || menu.getName().isBlank()) {
            return List.of();
        }

        if (menu.getId() != null && menu.getId() > 0) {

            var optionalDBMenu = menuRepository.get(menu.getId());

            if (optionalDBMenu.isPresent()) {
                var dbMenu = optionalDBMenu.get();

                if (menu.getName().equalsIgnoreCase(dbMenu.getName())) {
                    return List.of();
                }
            }
        }

        if (menuRepository.exists(menu.getName())) {
            return List.of(new ValidationError("Name must be unique"));
        }

        return List.of();
    }
}
