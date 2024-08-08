package application.category.retrieve.list;

import com.glauco.codeflix.application.category.retrieve.list.CategoryListOutput;
import com.glauco.codeflix.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategorySeachQuery;
import com.glauco.codeflix.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        reset(categoryGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories(){
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expetedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySeachQuery(expectedPage, expectedPerPage, expetedTerms, expectedSort, expectedDirection);

        final var categories = List.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Séries", null, true)
        );



        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedItemsCount, actualResult.total());


    }

    @Test
    void givenAnInvalidQuery_whenHasNoResults_thenShouldReturnAnEmptyCategories(){
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expetedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySeachQuery(expectedPage, expectedPerPage, expetedTerms, expectedSort, expectedDirection);

        final var categories = List.<Category>of();

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedItemsCount, actualResult.total());

    }

    @Test
    void givenAnValidQuery_whenHGatewayThrowsException_thenShouldReturnException(){
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expetedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expetedErrorMessage = "Gateway Error";

        final var aQuery = new CategorySeachQuery(expectedPage, expectedPerPage, expetedTerms, expectedSort, expectedDirection);

        when(categoryGateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException(expetedErrorMessage));

        final var actualResult = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        assertEquals(expetedErrorMessage, actualResult.getMessage());

    }
}
