package application.category.delete;

import com.glauco.codeflix.application.category.delete.DefaultDeleteCategoryUseCase;
import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));

    }

    @Test
    void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedId = CategoryID.from(UUID.randomUUID());

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.from(UUID.randomUUID());

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deleteById(eq(expectedId));

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }
}
