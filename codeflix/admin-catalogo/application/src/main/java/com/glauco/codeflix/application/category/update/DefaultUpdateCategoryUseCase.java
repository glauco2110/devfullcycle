package com.glauco.codeflix.application.category.update;

import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategoryID;
import com.glauco.codeflix.domain.exceptions.DomainException;
import com.glauco.codeflix.domain.validation.Error;
import com.glauco.codeflix.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway aGateway) {
        this.gateway = Objects.requireNonNull(aGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = this.gateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();

        aCategory.update(aName, aDescription, isActive).validate(notification);

        return notification.hasError() ? Either.left(notification) : update(aCategory);
    }


    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return API.Try(() -> this.gateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }
}
