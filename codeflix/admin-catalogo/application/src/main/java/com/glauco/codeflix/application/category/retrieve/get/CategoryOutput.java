package com.glauco.codeflix.application.category.retrieve.get;

import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(CategoryID id,
                             String name,
                             String description,
                             boolean isActive,
                             Instant createdAt,
                             Instant updatedAt,
                             Instant deletedAt) {

    public static CategoryOutput from(Category aCategory) {
        return new CategoryOutput(aCategory.getId(),
                                  aCategory.getName(),
                                  aCategory.getDescription(),
                                  aCategory.isActive(),
                                  aCategory.getCreatedAt(),
                                  aCategory.getUpdatedAt(),
                                  aCategory.getDeletedAt());
    }
}
