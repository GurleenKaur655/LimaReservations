package nbcc.common.result;

import java.util.Collection;
import java.util.List;
import nbcc.common.validation.ValidationError;

public interface Result<T> {

    T getValue();

    default Collection<ValidationError> getValidationErrors(){
        return List.of();
    }

    default boolean isEmpty() {
        return !hasValue();
    }

    default boolean hasValue(){
        return getValue() != null;
    }

    default boolean hasValidationErrors() {

        if(getValidationErrors() == null){
            return false;
        }

        return !getValidationErrors().isEmpty();
    }

    default boolean isSuccessful() {
        return !hasValidationErrors();
    }

    default boolean isError() {
        return false;
    }
}
