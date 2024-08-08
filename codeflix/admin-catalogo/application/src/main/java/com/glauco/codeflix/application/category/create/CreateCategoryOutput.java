package com.glauco.codeflix.application.category.create;

import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryID;

public record CreateCategoryOutput(CategoryID id) {
    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
