package com.glauco.codeflix.application.category.update;

import com.glauco.codeflix.application.UseCase;
import com.glauco.codeflix.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
