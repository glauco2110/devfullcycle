package com.glauco.codeflix.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final Error anError);

    ValidationHandler append(final ValidationHandler anHandler);

    ValidationHandler validate(final Validation aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    interface Validation {
        void validate();
    }

    default  Error firstError() {
        return getErrors() != null && !getErrors().isEmpty() ? getErrors().get(0) : null;
    }
}
