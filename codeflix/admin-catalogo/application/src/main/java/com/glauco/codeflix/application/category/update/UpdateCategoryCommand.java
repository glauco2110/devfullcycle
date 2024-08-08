package com.glauco.codeflix.application.category.update;

public record UpdateCategoryCommand(String id, String name, String description, boolean isActive) {

    public static UpdateCategoryCommand with(final String anId, final String name, final String description, final boolean isActive) {
        return new UpdateCategoryCommand(anId, name, description, isActive);
    }
}
