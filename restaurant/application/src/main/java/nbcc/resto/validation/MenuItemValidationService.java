package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Menu;
import nbcc.resto.dto.MenuItem;
import nbcc.resto.repository.MenuItemRepository;
import nbcc.resto.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MenuItemValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final MenuItemRepository menuItemRepository;

    public MenuItemValidationService(AnnotationValidationService annotationValidationService, MenuItemRepository menuItemRepository) {
        this.annotationValidationService = annotationValidationService;
        this.menuItemRepository = menuItemRepository;
    }

    public Collection<ValidationError> validate(MenuItem menuItem) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(menuItem));
        errors.addAll(validateUniqueName(menuItem));
        return errors;
    }


    public Collection<ValidationError> validateUniqueName(MenuItem menuItem) {
        if (menuItem.getName() == null || menuItem.getName().isBlank()) {
            return List.of();
        }

        if (menuItem.getId() != null && menuItem.getId() > 0) {

            var optionalDBMenuItem = menuItemRepository.get(menuItem.getId());

            if (optionalDBMenuItem.isPresent()) {
                var dbMenuItem = optionalDBMenuItem.get();

                if (menuItem.getName().equalsIgnoreCase(dbMenuItem.getName())) {
                    return List.of();
                }
            }
        }

        if (menuItemRepository.exists(menuItem.getName())) {
            return List.of(new ValidationError("Name must be unique"));
        }

        return List.of();
    }
}
