import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class StaffFlagControllerTest {

    private StaffFlagController controller;

    @Before
    public void setUp() {
        controller = new StaffFlagController();
    }

    @Test
    public void testResolveFlag() {
        boolean result = controller.resolveFlag("question", 101);
        assertTrue(result);
    }

    @Test
    public void testDeleteContent() {
        boolean result = controller.deleteContent("answer", 204);
        assertTrue(result);
    }
}
