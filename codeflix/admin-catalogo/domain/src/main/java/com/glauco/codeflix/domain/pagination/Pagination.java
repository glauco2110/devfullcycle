package com.glauco.codeflix.domain.pagination;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Pagination<T>(
        int currentPage,
        int perPage,
        long total,
        List<T> items
) {

    public <R> Pagination<R> map(final Function<T, R> mapper){
        final List<R> aNewList = this.items.stream().map(mapper).collect(Collectors.toList());
        return new Pagination<>(currentPage(), perPage(), total(), aNewList);
    }
}
