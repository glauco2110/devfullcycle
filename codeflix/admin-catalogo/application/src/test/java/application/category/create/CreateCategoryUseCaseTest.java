package application.category.create;

import com.glauco.codeflix.application.category.create.CreateCategoryCommand;
import com.glauco.codeflix.application.category.create.DefaultCreateCategoryUseCase;
import com.glauco.codeflix.domain.category.CategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        reset(categoryGateway);
    }

    // Teste do caminho feliz
    // Teste passando uma propriedade invalida (mame)
    // Teste criando uma categoria inativa
    // Teste simulando um erro generico vindo do gateway

    @Test
    public void giverAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(Mockito.argThat(category -> {
            return Objects.equals(category.getName(), expectedName) &&
                    Objects.equals(category.getDescription(), expectedDescription) &&
                    category.isActive() == expectedIsActive &&
                    Objects.nonNull(category.getId()) &&
                    Objects.nonNull(category.getCreatedAt()) &&
                    Objects.nonNull(category.getUpdatedAt()) &&
                    Objects.isNull(category.getDeletedAt());
        }));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShoultReturnDomainException(){

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, never()).create(any());

    }

    @Test
    public void giverAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(Mockito.argThat(category -> {
            return Objects.equals(category.getName(), expectedName) &&
                    Objects.equals(category.getDescription(), expectedDescription) &&
                    category.isActive() == expectedIsActive &&
                    Objects.nonNull(category.getId()) &&
                    Objects.nonNull(category.getCreatedAt()) &&
                    Objects.nonNull(category.getUpdatedAt()) &&
                    Objects.nonNull(category.getDeletedAt());
        }));
    }

    @Test
    public void giverAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway Error";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway Error"));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).create(any());
    }

}
