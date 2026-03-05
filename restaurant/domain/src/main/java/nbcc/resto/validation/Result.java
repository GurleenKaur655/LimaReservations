package nbcc.resto.validation;

import java.util.Collection;
import java.util.List;

public interface Result<T> {
    T getValue();

    default boolean isEmpty(){
        return getValue() == null;
    }

    default boolean hasValue() {
        return getValue() != null;
    }

    default Collection<ValidationError> getErrors(){
        return List.of();
    }

    default boolean isError() {
        return !getErrors().isEmpty();
    }

    default boolean isSuccessful() {
        return !isError();
    }

    default boolean isUnexpectedFailure() {
        return false;
    }

//     +getValue() T
//            +isEmpty() boolean
//            +hasValue() boolean
//            +isSuccessful() boolean
//            +isError() boolean
}
