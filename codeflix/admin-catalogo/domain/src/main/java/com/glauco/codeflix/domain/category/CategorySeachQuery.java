package com.glauco.codeflix.domain.category;

public record CategorySeachQuery(int page, int perPage, String terms, String sort, String direction) {
}
