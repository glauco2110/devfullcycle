package infrastructure;

import com.glauco.codeflix.infrastructure.Main;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.AbstractEnvironment;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTest {

    @Test
    public void testMain(){
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
        assertNotNull(new Main());
        Main.main(new String[]{});
    }
}