package application.category.update;

import com.glauco.codeflix.application.category.update.DefaultUpdateCategoryUseCase;
import com.glauco.codeflix.application.category.update.UpdateCategoryCommand;
import com.glauco.codeflix.domain.category.Category;
import com.glauco.codeflix.domain.category.CategoryGateway;
import com.glauco.codeflix.domain.category.CategoryID;
import com.glauco.codeflix.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        reset(categoryGateway);
    }

    @Test
    public void giverAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1)).update(Mockito.argThat(aUpdatedCategory ->
                Objects.equals(aUpdatedCategory.getName(), expectedName) &&
                        Objects.equals(aUpdatedCategory.getDescription(), expectedDescription) &&
                        aUpdatedCategory.isActive() == expectedIsActive &&
                        Objects.equals(aUpdatedCategory.getId(), expectedId) &&
                        Objects.equals(aUpdatedCategory.getCreatedAt(), aCategory.getCreatedAt()) &&
                        aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()) &&
                        Objects.isNull(aUpdatedCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShoultReturnDomainException(){
        final var aCategory = Category.newCategory("Film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, never()).update(any());

    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactivedCategoryId(){
        final var aCategory = Category.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1)).update(Mockito.argThat(aUpdatedCategory ->
                Objects.equals(aUpdatedCategory.getName(), expectedName) &&
                        Objects.equals(aUpdatedCategory.getDescription(), expectedDescription) &&
                        aUpdatedCategory.isActive() == expectedIsActive &&
                        Objects.equals(aUpdatedCategory.getId(), expectedId) &&
                        Objects.equals(aUpdatedCategory.getCreatedAt(), aCategory.getCreatedAt()) &&
                        aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()) &&
                        Objects.nonNull(aUpdatedCategory.getDeletedAt())
        ));

    }

    @Test
    public void giverAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = Category.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway Error";
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).update(any());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = UUID.randomUUID().toString();

        final var expetedErrorMessage = "Category with ID %s was not found".formatted(expectedId);
        final var expetedErrorCount = 1;


        final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(CategoryID.from(expectedId))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(expetedErrorCount, actualException.getErrors().size());
        assertEquals(expetedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).findById(CategoryID.from(expectedId));

        verify(categoryGateway, never()).update(any());

    }
}
