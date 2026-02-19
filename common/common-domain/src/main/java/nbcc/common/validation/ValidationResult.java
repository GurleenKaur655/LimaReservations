package nbcc.common.validation;

import nbcc.common.result.Result;

import java.util.Collection;
import java.util.List;

public class ValidationResult<T> implements Result<T> {

    private final T value;

    private final Collection<ValidationError> errors;

    ValidationResult() {
        this(null, List.of());
    }

    protected ValidationResult(T value) {
        this(value,  List.of());
    }

    protected ValidationResult(T value, Collection<ValidationError> errors) {
        this.value = value;
        this.errors = errors;
    }

    protected ValidationResult(Collection<ValidationError> errors) {
        this(null, errors);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public Collection<ValidationError> getValidationErrors() {
        return errors;
    }

    public static <T> Result<T> valid(){
        return new ValidationResult<>();
    }

    public static <T> Result<T> valid(T data){
        return new ValidationResult<>(data);
    }

    public static <T> Result<T> error(T data, Collection<ValidationError> validationErrors) {
        return new ValidationResult<>(data, validationErrors);
    }

    public static <T> Result<T> error(T data, ValidationError ... validationErrors) {
        var errors = List.of(validationErrors);
        return new ValidationResult<>(data, errors);
    }

    public static <T> Result<T> error(Collection<ValidationError> validationErrors) {
        return new ValidationResult<>(validationErrors);
    }

    public static <T> Result<T> error(ValidationError ... validationErrors) {
        return error(List.of(validationErrors));
    }

    public static <T> Result<T> error(String message) {
        return error(new ValidationError(message));
    }

    public static <T> Result<T> error(String message, String field) {
        return error(new ValidationError(message, field));
    }

    public static <T> Result<T> error(String message, String field, Object value) {
        return error(new ValidationError(message, field, value));
    }
}