package nbcc.common.validation;

import java.util.Collection;

public interface ValidationResponse {

    Collection<ValidationError> getValidationErrors();

    default boolean hasValidationErrors(){
        return getValidationErrors() != null && !getValidationErrors().isEmpty();
    }
}
