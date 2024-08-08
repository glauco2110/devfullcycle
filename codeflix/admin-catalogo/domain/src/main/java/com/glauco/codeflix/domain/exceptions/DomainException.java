package com.glauco.codeflix.domain.exceptions;

import com.glauco.codeflix.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException{

    private final List<Error> errors;
    private DomainException (final String aMessage, final List<Error> errors) {
        super(aMessage);
        this.errors = errors;
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("",errors);
    }

    public static DomainException with(final Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    public List<Error> getErrors() {
        return this.errors;
    }
}
