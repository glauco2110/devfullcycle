package infrastructure.category;

import com.glauco.codeflix.infrastructure.category.CategoryMySQLGateway;
import com.glauco.codeflix.infrastructure.category.persistence.CategoryRepository;
import infrastructure.MySQLGatewayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
