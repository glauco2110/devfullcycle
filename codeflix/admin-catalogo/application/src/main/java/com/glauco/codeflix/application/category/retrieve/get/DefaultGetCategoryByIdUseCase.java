package com.glauco.codeflix.application.category.retrieve.get;

import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategoryID;
import com.glauco.codeflix.domain.exceptions.DomainException;
import com.glauco.codeflix.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway gateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        final var id = CategoryID.from(anId);
        return this.gateway.findById(id)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(id));
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }
}
