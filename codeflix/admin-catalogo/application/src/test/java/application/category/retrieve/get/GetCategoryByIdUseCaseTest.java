package application.category.retrieve.get;

import com.glauco.codeflix.application.category.retrieve.get.CategoryOutput;
import com.glauco.codeflix.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategoryID;
import com.glauco.codeflix.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        assertEquals(CategoryOutput.from(aCategory), actualCategory);
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.isActive());
    }

    @Test
    void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedId = CategoryID.from(UUID.randomUUID());
        final var expectedMessage = "Category with ID %s was not found".formatted(expectedId.getValue());

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedMessage = "Gateway Error";
        final var expectedId = CategoryID.from(UUID.randomUUID());

        when(categoryGateway.findById(eq(expectedId))).thenThrow(new IllegalStateException(expectedMessage));

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedMessage, actualException.getMessage());
    }
}
