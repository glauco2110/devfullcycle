package com.glauco.codeflix.application.category.delete;

import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway aCategoryGateway) {
        this.categoryGateway = Objects.requireNonNull(aCategoryGateway);
    }

    @Override
    public void execute(final String anId) {
        this.categoryGateway.deleteById(CategoryID.from(anId));
    }

}
