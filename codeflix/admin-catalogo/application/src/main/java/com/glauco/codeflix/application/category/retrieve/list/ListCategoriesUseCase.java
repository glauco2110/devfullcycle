package com.glauco.codeflix.application.category.retrieve.list;

import com.glauco.codeflix.application.UseCase;
import com.glauco.codeflix.domain.category.CategorySeachQuery;
import com.glauco.codeflix.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySeachQuery, Pagination<CategoryListOutput>> {
}
