package nbcc.common.validation;

import nbcc.common.result.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ModelErrorConverter {

    public static <T> BindingResult addErrorsToBindingResults(BindingResult bindingResult, Result<T> result) {
        return addErrorsToBindingResults(result.getValue().getClass().getSimpleName(), bindingResult, result.getValidationErrors());
    }

    public static BindingResult addErrorsToBindingResults(String objectName, BindingResult bindingResult, Iterable<ValidationError> errors) {
        for (ValidationError error : errors) {
            if (error.getField() == null || error.getField().isEmpty()) {
                bindingResult.addError(new ObjectError(objectName, error.getMessage()));
            } else {
               //bindingResult.addError(new FieldError(objectName, error.getField(), error.getMessage()));
               bindingResult.addError(new FieldError(objectName, error.getField(), error.getValue(), false, null, null, error.getMessage()));
            }
        }

        return bindingResult;
    }
}
