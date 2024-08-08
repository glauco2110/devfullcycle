package com.glauco.codeflix.application.category.create;

import com.glauco.codeflix.application.UseCase;
import com.glauco.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
