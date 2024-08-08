package infrastructure;

import com.glauco.codeflix.infrastructure.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MainTest {

    @Test
    public void testMain(){
        assertNotNull(new Main());
        Main.main(new String[]{});
    }
}