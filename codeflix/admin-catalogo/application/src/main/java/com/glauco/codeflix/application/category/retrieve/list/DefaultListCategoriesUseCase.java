package com.glauco.codeflix.application.category.retrieve.list;

import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategorySeachQuery;
import com.glauco.codeflix.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySeachQuery anIn) {
        return this.categoryGateway.findAll(anIn).map(CategoryListOutput::from);
    }
}
