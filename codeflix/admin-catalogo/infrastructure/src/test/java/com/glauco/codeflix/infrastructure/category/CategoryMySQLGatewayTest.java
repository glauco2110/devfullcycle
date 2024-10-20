package com.glauco.codeflix.infrastructure.category;

import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.infrastructure.MySQLGatewayTest;
import com.glauco.codeflix.infrastructure.category.persistence.CategoryJpaEntity;
import com.glauco.codeflix.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    void testInjectedDependencies() {
       assertNotNull(categoryGateway);
       assertNotNull(categoryRepository);
    }

    @Test
    void giverAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expetedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory= Category.newCategory(expetedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(aCategory);
        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.isActive(), actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull( actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(aCategory.getName(), actualEntity.getName());
        assertEquals(aCategory.getDescription(), actualEntity.getDescription());
        assertEquals(aCategory.isActive(), actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertNull(actualEntity.getDeletedAt());

    }

    @Test
    void giverAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expetedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory= Category.newCategory("Film", null, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

        final var aUpdatedCategory = aCategory.clone().update(expetedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryGateway.update(aUpdatedCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.isActive(), actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull( actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId(), actualEntity.getId());
        assertEquals(aCategory.getName(), actualEntity.getName());
        assertEquals(aCategory.getDescription(), actualEntity.getDescription());
        assertEquals(aCategory.isActive(), actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertNull(actualEntity.getDeletedAt());



    }

}
