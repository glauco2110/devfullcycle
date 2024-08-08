package com.glauco.codeflix.application.category.update;

import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryID;

public record UpdateCategoryOutput(CategoryID id) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
